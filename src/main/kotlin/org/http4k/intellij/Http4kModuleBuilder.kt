package org.http4k.intellij

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import org.http4k.intellij.step.QuestionnaireStep
import org.http4k.intellij.utils.backgroundTask
import org.http4k.intellij.utils.createRunConfiguration
import org.http4k.intellij.utils.setNextTo
import org.http4k.intellij.utils.unzipInto
import org.http4k.intellij.wizard.StackId
import org.http4k.intellij.wizard.ToolboxApi
import org.jetbrains.plugins.gradle.service.project.GradleAutoImportAware
import org.jetbrains.plugins.gradle.service.project.open.linkAndRefreshGradleProject
import java.io.File
import java.util.concurrent.atomic.AtomicReference

class Http4kModuleBuilder : ModuleBuilder() {
    private val api = ToolboxApi(debug = false)

    private val stackId = AtomicReference<StackId>()

    override fun getCustomOptionsStep(context: WizardContext, parentDisposable: Disposable) =
        QuestionnaireStep(api.questionnaire(), context) {
            stackId.set(api.stackIdFor(it.first()))
            context.setNextTo(true)
        }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val root = createAndGetRoot() ?: return

        modifiableRootModel.apply {
            addContentEntry(root)
            project.backgroundTask("Setting up project") {
                api.generateProject(stackId.get()).unzipInto(File(root.path))

                modifiableRootModel.project.createRunConfiguration()
                GradleAutoImportAware()
                invokeLater {
                    linkAndRefreshGradleProject(root.canonicalPath + "/build.gradle.kts", project)
                }
            }
        }

    }

    private fun createAndGetRoot() = contentEntryPath
        ?.let(FileUtil::toSystemIndependentName)
        ?.let(::File)
        ?.apply { mkdirs() }?.absolutePath
        ?.let(LocalFileSystem.getInstance()::refreshAndFindFileByPath)

    override fun getModuleType() = Http4kModuleType()
}
