package org.http4k.intellij.utils

import com.intellij.ide.projectWizard.NewProjectWizard
import com.intellij.ide.util.projectWizard.WizardContext
import javax.swing.SwingUtilities

fun WizardContext.setNextTo(enabled: Boolean) {
    SwingUtilities.invokeLater {
        val get = get()
        val newProjectWizard = get[get.keys.first { it.toString() == "AbstractWizard" }] as NewProjectWizard
        newProjectWizard.updateButtons(false, enabled, true)
    }
}
