package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer
import org.http4k.intellij.wizard.Step
import java.awt.GridBagLayout
import java.util.concurrent.atomic.AtomicReference
import javax.swing.JPanel

fun ChildStepsView(steps: List<Step>, parent: JPanel, onComplete: OnComplete): JPanel {
    val allAnswers = mutableListOf<Answer>()
    val callback = AtomicReference<OnComplete>()

    val panel = JPanel(GridBagLayout())

    callback.set { new ->
        allAnswers += new
        when (allAnswers.size) {
            steps.size -> {
                parent.remove(panel)
                onComplete(allAnswers)
            }

            else -> panel.addMax(StepView(steps[allAnswers.size], panel, callback.get()))
        }
        parent.revalidate()
    }

    return panel.apply {
        addMax(StepView(steps.first(), panel, callback.get()))
    }
}
