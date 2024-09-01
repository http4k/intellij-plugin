package org.http4k.intellij.utils

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

fun Project.backgroundTask(name: String, callback: (indicator: ProgressIndicator) -> Unit) {
    ProgressManager.getInstance().run(object : Task.Backgroundable(this, name, false) {
        override fun shouldStartInBackground() = false

        override fun run(indicator: ProgressIndicator) = try {
            indicator.isIndeterminate = true
            callback(indicator)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    })
}
