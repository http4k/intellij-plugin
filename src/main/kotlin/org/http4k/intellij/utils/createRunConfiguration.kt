package org.http4k.intellij.utils

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType
import org.jetbrains.plugins.gradle.service.execution.GradleRunConfiguration
import javax.swing.Icon

fun Project.createRunConfiguration() {
    val runManager = RunManagerImpl.getInstanceImpl(this)
    runManager.addConfiguration(
        RunnerAndConfigurationSettingsImpl(
            RunManagerImpl.getInstanceImpl(this),
            Http4kConfigurationFactory("jvmRun").createTemplateConfiguration(this)
        )
    )
    runManager.setOrder({ o1, o2 ->
        when {
            o1?.factory is Http4kConfigurationFactory -> 1
            o2?.factory is Http4kConfigurationFactory -> -1
            else -> 0
        }
    })
    runManager.requestSort()
}

private class Http4kConfigurationFactory(val task: String, private val args: String = "") :
    ConfigurationFactory(GradleExternalTaskConfigurationType.getInstance()) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        val conf = GradleRunConfiguration(
            project,
            GradleExternalTaskConfigurationType.getInstance().factory,
            "Run $task"
        )
        conf.settings.externalProjectPath = project.basePath
        conf.settings.taskNames = listOf(task)
        conf.settings.scriptParameters = args
        return conf
    }

    override fun getName(): String = "Run $task"

    override fun getIcon(): Icon = IconLoader.getIcon("/images/http4k.png", Http4kConfigurationFactory::class.java)
}
