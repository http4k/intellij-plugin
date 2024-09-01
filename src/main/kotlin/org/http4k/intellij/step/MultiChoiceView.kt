package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer
import org.http4k.intellij.wizard.Step
import java.awt.BorderLayout.CENTER
import java.awt.GridLayout
import javax.swing.JCheckBox
import javax.swing.JPanel

fun MultiChoiceView(multiChoice: Step.MultiChoice, parent: JPanel, onComplete: OnComplete): JPanel {

    val panel = QuestionPanel(multiChoice.label, true)

    return panel.apply {
        val selected = multiChoice.options.filter { it.default }.toMutableSet()

        val optionsPanel = JPanel().apply {
            layout = GridLayout(0, 4, 10, 10)
        }

        multiChoice.options.forEach { option ->
            val checkBox = JCheckBox().apply {
                isSelected = option.default
                addActionListener {
                    if (isSelected) selected.add(option) else selected.remove(option)
                }
            }

            optionsPanel.add(OptionBox(checkBox, option))
        }

        panel.add(optionsPanel, CENTER)

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


