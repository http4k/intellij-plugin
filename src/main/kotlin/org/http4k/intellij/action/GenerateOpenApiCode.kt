package org.http4k.intellij.action

import com.intellij.openapi.actionSystem.ActionUpdateThread.BGT
import com.intellij.openapi.actionSystem.ActionUpdateThreadAware
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.impl.status.StatusBarUtil
import dev.forkhandles.result4k.onFailure
import dev.forkhandles.result4k.orThrow
import org.http4k.format.Jackson
import org.http4k.format.JacksonYaml
import org.http4k.intellij.utils.unzipInto
import org.http4k.intellij.wizard.ClientApiStyle
import org.http4k.intellij.wizard.ClientApiStyle.connect
import org.http4k.intellij.wizard.ClientApiStyle.standard
import org.http4k.intellij.wizard.ToolboxApi
import java.io.File
import java.time.Clock
import java.time.temporal.ChronoUnit.SECONDS

open class GenerateOpenApiCode(private val style: ClientApiStyle) : AnAction(), ActionUpdateThreadAware {
    private val toolbox = ToolboxApi()
    override fun update(e: AnActionEvent) {
        val file = e.dataContext.getData(VIRTUAL_FILE)
        e.presentation.setEnabledAndVisible(file?.fileIsOpenApiSpec() ?: false)
    }

    override fun getActionUpdateThread() = BGT

    override fun actionPerformed(e: AnActionEvent) {
        val file = e.dataContext.getData(VIRTUAL_FILE) ?: return
        val http4kDir = File(e.project!!.basePath, ".http4k")
        val openApiDir = File(http4kDir, "openapi")

        val target = File(
            openApiDir,
            file.name + "-generated-" +
                Clock.systemUTC().instant().truncatedTo(SECONDS).toString().filter { it.isLetterOrDigit() }
        ).apply { mkdirs() }

        toolbox.generateOpenApiClasses(style, file.inputStream)
            .onFailure { it.orThrow() }
            .unzipInto(target)

        VirtualFileManager.getInstance().syncRefresh();

        updateStatusBar(e.project!!, target)
    }

    private fun updateStatusBar(project: Project, target: File) {
        StatusBarUtil.setStatusBarInfo(project, "OpenApi classes written to ${target.absolutePath}")
    }
}

class GenerateOpenApiCodeStandard : GenerateOpenApiCode(standard)
class GenerateOpenApiCodeConnect : GenerateOpenApiCode(connect)

private fun VirtualFile.fileIsOpenApiSpec(): Boolean {
    if (!(name.endsWith(".json") || name.endsWith(".yaml"))) return false
    else {
        val content = inputStream.reader().readText() ?: ""
        return try {
            Jackson.asA<Map<String, Any>>(content)
        } catch (e: Exception) {
            JacksonYaml.asA<Map<String, Any>>(content)
        }["openapi"] != null
    }
}
