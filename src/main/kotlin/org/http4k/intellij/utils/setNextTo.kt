package org.http4k.intellij.utils

import com.intellij.ide.projectWizard.NewProjectWizard
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.util.Key
import javax.swing.SwingUtilities

fun WizardContext.setNextTo(enabled: Boolean) = SwingUtilities.invokeLater {
    getUserData(Key<NewProjectWizard>("AbstractWizard"))?.updateButtons(false, enabled, true)
}
