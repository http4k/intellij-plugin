package org.http4k.intellij.utils

import com.intellij.compiler.options.CompileStepBeforeRun.MakeBeforeRunTask
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.impl.RunnerAndConfigurationSettingsImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import org.jetbrains.kotlin.idea.run.KotlinRunConfigurationType
import javax.swing.Icon

fun Project.createRunConfiguration(pkg: String, clazz: String) {
    val runManager = RunManagerImpl.getInstanceImpl(this)
    runManager.addConfiguration(
        RunnerAndConfigurationSettingsImpl(
            RunManagerImpl.getInstanceImpl(this),
            Http4kConfigurationFactory(pkg, clazz).createTemplateConfiguration(this)
        )
    )
    runManager.setOrder({ o1, o2 ->
        when {
            o1.name.startsWith("Run") -> -1
            o2.name.startsWith("Run") -> 1
            else -> 0
        }
    })
    runManager.requestSort()
}

private class Http4kConfigurationFactory(private val pkg: String, private val clazz: String) :
    ConfigurationFactory(KotlinRunConfigurationType()) {

    override fun createTemplateConfiguration(project: Project) =
        KotlinRunConfigurationType().createTemplateConfiguration(project).apply {
            name = "Run App"
            runClass = "$pkg.${clazz}Kt"
            setModuleName("$clazz.main")
            beforeRunTasks = listOf(MakeBeforeRunTask())
        }

    override fun getName(): String = "Run $clazz"

    override fun getIcon(): Icon = IconLoader.getIcon("/images/http4k.png", Http4kConfigurationFactory::class.java)
}

