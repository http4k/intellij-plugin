package org.http4k.intellij.step

import org.http4k.intellij.wizard.Step
import org.http4k.intellij.wizard.Step.Choice
import org.http4k.intellij.wizard.Step.Input
import org.http4k.intellij.wizard.Step.MultiChoice
import org.http4k.intellij.wizard.Step.Section
import javax.swing.JPanel

fun StepView(step: Step, parent: JPanel, onComplete: OnComplete) = when (step) {
    is Section -> SectionView(step, parent, onComplete)
    is Choice -> ChoiceView(step, parent, onComplete)
    is Input -> InputView(step, parent, onComplete)
    is MultiChoice -> MultiChoiceView(step, parent, onComplete)
}

