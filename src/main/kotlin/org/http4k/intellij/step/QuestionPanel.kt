package org.http4k.intellij.step

import java.awt.BorderLayout
import java.awt.BorderLayout.EAST
import java.awt.BorderLayout.NORTH
import java.awt.BorderLayout.WEST
import javax.swing.BorderFactory.createEmptyBorder
import javax.swing.JComponent
import javax.swing.JPanel

class QuestionPanel(question: String, nextEnabled: Boolean, vararg intermediate: JComponent) : JPanel(BorderLayout()) {
    val nextButton = NextButton(nextEnabled)

    init {
        border = createEmptyBorder(10, 10, 10, 10)

        add(
            JPanel(BorderLayout()).apply {
                add(question.label().bold(), WEST)
                intermediate.forEach(::add)
                add(nextButton, EAST)
            }, NORTH
        )
    }
}
