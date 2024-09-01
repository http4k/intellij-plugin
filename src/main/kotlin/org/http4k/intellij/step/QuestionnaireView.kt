package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer.Step
import org.http4k.intellij.wizard.Questionnaire
import java.awt.Color
import java.awt.GridBagConstraints
import java.awt.GridBagConstraints.HORIZONTAL
import java.awt.GridBagConstraints.NORTH
import java.awt.GridBagConstraints.REMAINDER
import java.awt.GridBagLayout
import javax.swing.BorderFactory.createLineBorder
import javax.swing.BorderFactory.createTitledBorder
import javax.swing.JPanel
import javax.swing.border.TitledBorder.CENTER
import javax.swing.border.TitledBorder.DEFAULT_POSITION

fun QuestionnaireView(
    questionnaire: Questionnaire,
    onComplete: OnComplete
) = JPanel().apply {
    layout = GridBagLayout()
    border = createTitledBorder(
        createLineBorder(Color.GRAY, 1), "    http4k Toolbox Project Wizard    ",
        CENTER,
        DEFAULT_POSITION,
        font.deriveFont(20f)
    )
    add(ChildStepsView(questionnaire.steps, this@apply) {
        parent.add(SummaryView(it))
        onComplete(listOf(Step("", steps = it)))
    }, GridBagConstraints().apply {
        fill = HORIZONTAL
        weightx = 1.0
        weighty = 1.0
        anchor = NORTH
        gridx = 0
        gridy = 0
        gridwidth = REMAINDER
    })
}
