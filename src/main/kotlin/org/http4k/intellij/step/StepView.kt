package org.http4k.intellij.step

import org.http4k.intellij.wizard.Step
import org.http4k.intellij.wizard.Step.Choice
import org.http4k.intellij.wizard.Step.Input
import org.http4k.intellij.wizard.Step.MultiChoice
import org.http4k.intellij.wizard.Step.Section
import javax.swing.JPanel

fun StepView(step: Step, parent: JPanel, onReset: OnReset, onComplete: OnComplete) = when (step) {
    is Section -> SectionView(step, parent, onReset, onComplete)
    is Choice -> ChoiceView(step, parent, onReset, onComplete)
    is Input -> InputView(step, parent, onReset, onComplete)
    is MultiChoice -> MultiChoiceView(step, parent, onReset, onComplete)
}

