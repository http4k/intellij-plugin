package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer
import org.http4k.intellij.wizard.Step
import javax.swing.BoxLayout
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.JCheckBox
import javax.swing.JPanel

fun MultiChoiceView(multiChoice: Step.MultiChoice, parent: JPanel, onReset: OnReset, onComplete: OnComplete): JPanel {

    val panel = QuestionPanel(multiChoice.label, true, onReset)

    return panel.apply {
        val selected = multiChoice.options.filter { it.default }.toMutableSet()

        val optionsPanel = JPanel().apply {
            layout = BoxLayout(this, Y_AXIS)
        }

        panel.add(optionsPanel)

        multiChoice.options
            .sortedBy { it.label }
            .forEach { option ->
                optionsPanel.add(OptionBox(JCheckBox().apply {
                    isSelected = option.default
                    addActionListener {
                        if (isSelected) selected.add(option) else selected.remove(option)
                    }
                }, option))
        }

        panel.nextButton.apply {
            addActionListener {
                onComplete(
                    listOf(
                        Answer.Step(
                            multiChoice.label,
                            selected.map { Answer.Text(it.label, emptyList(), emptyList()) },
                            listOf()
                        )
                    )
                )
                parent.remove(panel)
                parent.revalidate()
            }
        }
    }
}


