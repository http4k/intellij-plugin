package org.http4k.intellij

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.get
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.mapFailure
import dev.forkhandles.result4k.onFailure
import dev.forkhandles.result4k.orThrow
import kotlinx.coroutines.runBlocking
import org.http4k.intellij.step.FailedView
import org.http4k.intellij.step.QuestionnaireStep
import org.http4k.intellij.utils.backgroundTask
import org.http4k.intellij.utils.createRunConfiguration
import org.http4k.intellij.utils.setNextTo
import org.http4k.intellij.utils.unzipInto
import org.http4k.intellij.wizard.Answer
import org.http4k.intellij.wizard.Answer.Text
import org.http4k.intellij.wizard.ToolboxApi
import org.jetbrains.plugins.gradle.service.project.GradleAutoImportAware
import org.jetbrains.plugins.gradle.service.project.open.linkAndSyncGradleProject
import java.io.File
import java.util.concurrent.atomic.AtomicReference

class Http4kModuleBuilder : ModuleBuilder() {
    private val api = ToolboxApi(debug = false)

    private val answer = AtomicReference<Answer>()

    override fun getCustomOptionsStep(context: WizardContext, parentDisposable: Disposable) =
        api.questionnaire()
            .map {
                QuestionnaireStep(it, context) {
                    answer.set(it.first())
                    context.setNextTo(true)
                }
            }
            .mapFailure {
                object : ModuleWizardStep() {
                    override fun getComponent() = FailedView(it)

                    override fun updateDataModel() {
                    }
                }
            }
            .get()

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val root = createAndGetRoot() ?: return

        modifiableRootModel.apply {
            addContentEntry(root)
            project.backgroundTask("Setting up project") {
                api.stackIdFor(answer.get())
                    .flatMap(api::generateProject)
                    .onFailure { it.orThrow() }
                    .unzipInto(File(root.path))

                val (clazz, pkg) = answer.get().getClassAndPackage()
                modifiableRootModel.project.createRunConfiguration(pkg, clazz)
                GradleAutoImportAware()
                invokeLater {
                    runBlocking {
                        linkAndSyncGradleProject(project, root.canonicalPath + "/build.gradle.kts")
                    }
                }
            }
        }
    }

    private fun Answer.getClassAndPackage() =
        steps.dropLast(1).last().steps.filterIsInstance<Text>().map { it.answers.first() }

    private fun createAndGetRoot() = contentEntryPath
        ?.let(FileUtil::toSystemIndependentName)
        ?.let(::File)
        ?.apply { mkdirs() }?.absolutePath
        ?.let(LocalFileSystem.getInstance()::refreshAndFindFileByPath)

    override fun getModuleType() = Http4kModuleType()
}
