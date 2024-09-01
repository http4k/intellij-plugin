package org.http4k.intellij.utils

import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.http4k.intellij.Http4kModuleBuilder
import org.jetbrains.plugins.gradle.action.GradleExecuteTaskAction
import java.io.File


fun File.file(
    name: String,
    templateName: String,
    attributes: Map<String, Any> = emptyMap(),
    binary: Boolean = false,
    executable: Boolean = false
) {
    val file = File(this, name)
    if (!file.exists()) {
        file.createNewFile()
    }
    if (!binary) {
        val data = getTemplateData(templateName, attributes)
        file.writeText(data)
    } else {
        val data = getBinaryData(templateName)
        file.writeBytes(data)
    }
    if (executable) {
        file.setExecutable(true)
    }
}

private fun getTemplateData(templateName: String, attributes: Map<String, Any> = emptyMap()): String {
    val template = FileTemplateManager
        .getDefaultInstance()
        .getInternalTemplate(templateName)
    return if (attributes.isEmpty()) {
        template.text
    } else {
        template.getText(attributes)
    }
}

private fun getBinaryData(templateName: String): ByteArray {
    Http4kModuleBuilder::class.java.getResourceAsStream("/fileTemplates/internal/$templateName")
        .use { stream ->
            return stream!!.readBytes()
        }
}

fun Project.runGradle(command: String) {
    GradleExecuteTaskAction.runGradle(this, DefaultRunExecutor.getRunExecutorInstance(), basePath!!, command)
}

fun Project.getRootFile(): VirtualFile? {
    return projectFile?.parent?.parent
}

