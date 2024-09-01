package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer.Step
import org.http4k.intellij.wizard.Questionnaire
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.BorderFactory.createLineBorder
import javax.swing.BorderFactory.createTitledBorder
import javax.swing.JPanel
import javax.swing.border.TitledBorder.CENTER
import javax.swing.border.TitledBorder.DEFAULT_POSITION

fun QuestionnaireView(
    questionnaire: Questionnaire,
    onComplete: OnComplete
) = JPanel().apply {
    layout = FlowLayout()
    border = createTitledBorder(
        createLineBorder(Color.GRAY, 1), "    http4k Toolbox Project Wizard    ",
        CENTER,
        DEFAULT_POSITION,
        font.deriveFont(20f)
    )
    add(ChildStepsView(questionnaire.steps, this@apply) {
        parent.add(SummaryView(it))
        parent.revalidate()
        onComplete(listOf(Step("", steps = it)))
    })
}
