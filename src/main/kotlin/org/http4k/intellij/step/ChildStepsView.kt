package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer
import org.http4k.intellij.wizard.Step
import java.awt.FlowLayout
import java.util.concurrent.atomic.AtomicReference
import javax.swing.JPanel
import javax.swing.JPanel.LEFT_ALIGNMENT

fun ChildStepsView(steps: List<Step>, parent: JPanel, onComplete: OnComplete): JPanel {
    val allAnswers = mutableListOf<Answer>()
    val callback = AtomicReference<OnComplete>()

    val panel = JPanel().apply {
        layout = FlowLayout()
        alignmentX = LEFT_ALIGNMENT
    }

    callback.set { new ->
        allAnswers += new
        when (allAnswers.size) {
            steps.size -> {
                parent.remove(panel)
                onComplete(allAnswers)
            }

            else -> panel.add(StepView(steps[allAnswers.size], panel, callback.get()))
        }
        parent.revalidate()
    }

    return panel.apply {
        add(StepView(steps.first(), this, callback.get()))
    }
}
