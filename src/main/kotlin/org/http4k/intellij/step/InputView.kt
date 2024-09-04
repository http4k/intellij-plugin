package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer
import org.http4k.intellij.wizard.Step
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JPanel
import javax.swing.JTextField

fun InputView(input: Step.Input, parent: JPanel, onComplete: OnComplete): JPanel {

    val selection = JTextField(input.default).apply {
        columns = 25
    }

    val panel = QuestionPanel(input.label, true, selection)
    return panel.apply {
        selection.apply {
            addKeyListener(object : KeyAdapter() {
                override fun keyReleased(e: KeyEvent) {
                    panel.nextButton.isEnabled = text.isNotBlank()
                }
            })
        }

        panel.nextButton.apply {
            addActionListener {
                onComplete(listOf(Answer.Text(input.label, listOf(selection.text))))
                parent.remove(panel)
                parent.revalidate()
            }
        }
    }
}
