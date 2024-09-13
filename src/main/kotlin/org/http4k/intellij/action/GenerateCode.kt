package org.http4k.intellij.action

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.ActionUpdateThread.BGT
import com.intellij.openapi.actionSystem.ActionUpdateThreadAware
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.impl.status.StatusBarUtil
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.onFailure
import dev.forkhandles.result4k.orThrow
import dev.forkhandles.result4k.resultFrom
import dev.forkhandles.result4k.valueOrNull
import org.http4k.cloudnative.RemoteRequestFailed
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.format.Jackson
import org.http4k.format.JacksonYaml
import org.http4k.intellij.utils.unzipInto
import org.http4k.intellij.wizard.ClientApiStyle.connect
import org.http4k.intellij.wizard.ClientApiStyle.standard
import org.http4k.intellij.wizard.GeneratorFormat
import org.http4k.intellij.wizard.ToolboxApi
import java.io.File
import java.io.InputStream
import java.time.Clock
import java.time.temporal.ChronoUnit.SECONDS

abstract class GenerateCode(private val functionName: String) : AnAction(), ActionUpdateThreadAware {
    override fun update(e: AnActionEvent) {
        val file = e.dataContext.getData(VIRTUAL_FILE)
        e.presentation.setEnabledAndVisible(activeFor(file))
    }

    override fun getActionUpdateThread() = BGT

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project  ?: return
        val file = e.dataContext.getData(VIRTUAL_FILE) ?: return
        val http4kDir = File(project.basePath, ".http4k")
        val functionDir = File(http4kDir, functionName)

        val name = file.name + "-generated-" +
            Clock.systemDefaultZone().instant().truncatedTo(SECONDS).toString().filter { it.isLetterOrDigit() }

        val target = File(
            functionDir,
            name
        ).apply { mkdirs() }

        file.generateCode()
            .onFailure { it.orThrow() }
            .unzipInto(target)

        VirtualFileManager.getInstance().syncRefresh();

        val root = project.guessProjectDir()
            ?.findChild(".http4k")
            ?.findChild(functionName)
            ?.findChild(name)

        ProjectView.getInstance(project).select(null, root, true);

        updateStatusBar(e.project!!, target)
    }

    abstract fun VirtualFile.generateCode(): Result<InputStream, RemoteRequestFailed>

    private fun updateStatusBar(project: Project, target: File) {
        StatusBarUtil.setStatusBarInfo(project, "OpenApi classes written to ${target.absolutePath}")
    }

    abstract fun activeFor(selected: VirtualFile?): Boolean

}

class GenerateOpenApiCodeStandard : GenerateCode("openapi") {
    override fun VirtualFile.generateCode() =
        ToolboxApi().generateOpenApiClasses(standard, inputStream)

    override fun activeFor(selected: VirtualFile?) = isOpenApi(selected)
}

class GenerateOpenApiCodeConnect : GenerateCode("openapi") {
    override fun VirtualFile.generateCode() =
        ToolboxApi().generateOpenApiClasses(connect, inputStream)

    override fun activeFor(selected: VirtualFile?) = isOpenApi(selected)
}

class GenerateDataClasses : GenerateCode("dataclasses") {
    override fun VirtualFile.generateCode(): Result<InputStream, RemoteRequestFailed> {
        val format = supportedFormat() ?: return Failure(RemoteRequestFailed(BAD_REQUEST, "Unsupported format"))
        return ToolboxApi().generateDataClasses(format, inputStream)
    }

    override fun activeFor(selected: VirtualFile?) = selected?.supportedFormat() != null
}

private fun VirtualFile.supportedFormat() = resultFrom {
    GeneratorFormat.valueOf(name.substringAfterLast("."))
}.valueOrNull()

private fun isOpenApi(file: VirtualFile?): Boolean {
    if (file == null || file.supportedFormat() == null) return false
    else {
        val content = file.inputStream.reader().readText() ?: ""
        return try {
            Jackson.asA<Map<String, Any>>(content)
        } catch (e: Exception) {
            JacksonYaml.asA<Map<String, Any>>(content)
        }["openapi"] != null
    }
}
