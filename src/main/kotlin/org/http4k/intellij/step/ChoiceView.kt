package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer
import org.http4k.intellij.wizard.Option
import org.http4k.intellij.wizard.Step
import javax.swing.BoxLayout
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.ButtonGroup
import javax.swing.JPanel
import javax.swing.JRadioButton

fun ChoiceView(choice: Step.Choice, parent: JPanel, onReset: OnReset, onComplete: OnComplete): JPanel {
    val childAnswers = mutableListOf<Answer>()
    var selected = choice.options.first { it.default }

    val panel = QuestionPanel(choice.label, false, onReset)

    panel.nextButton.apply {
        addActionListener {
            when {
                selected.steps.isEmpty() ->
                    onComplete(choice.answersFor(selected, childAnswers))

                else -> parent.addMax(ChildStepsView(selected.steps, parent, onReset) {
                    onComplete(choice.answersFor(selected, childAnswers.toList() + it))
                })
            }
            parent.remove(panel)
            parent.revalidate()
        }
    }

    return panel.apply {
        val buttonGroup = ButtonGroup()

        val optionsPanel = JPanel().apply {
            layout = BoxLayout(this, Y_AXIS)
        }

        choice.options
            .sortedBy { if (it.default) "0" else "1" + it.label }
            .forEach { option ->
                val button = JRadioButton().apply {
                    if (option.default) isSelected = true
                    addActionListener {
                        selected = option
                        nextButton.isEnabled = true
                    }
                }
                buttonGroup.add(button)
                optionsPanel.add(OptionBox(button, option))
            }

        panel.add(optionsPanel)

    }
}

private fun Step.Choice.answersFor(selected: Option, childAnswers: List<Answer>) = listOf(
    Answer.Step(label, listOf(Answer.Text(selected.label, steps = childAnswers)))
)
