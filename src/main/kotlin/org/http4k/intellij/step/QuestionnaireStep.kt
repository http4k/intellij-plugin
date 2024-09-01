package org.http4k.intellij.step

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import org.http4k.intellij.utils.setNextTo
import org.http4k.intellij.wizard.Questionnaire

class QuestionnaireStep(
    private val questionnaire: Questionnaire,
    private val wizardContext: WizardContext,
    private val onComplete: OnComplete
) : ModuleWizardStep() {

    init {
        wizardContext.setNextTo(false)
    }

    override fun getComponent() = QuestionnaireView(questionnaire, onComplete)

    override fun updateDataModel() {
        wizardContext.setNextTo(true)
    }
}

