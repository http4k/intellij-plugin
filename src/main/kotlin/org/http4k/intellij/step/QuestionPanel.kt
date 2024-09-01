package org.http4k.intellij.step

import java.awt.BorderLayout
import java.awt.BorderLayout.NORTH
import java.awt.FlowLayout
import java.awt.FlowLayout.LEFT
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.JComponent
import javax.swing.JPanel

class QuestionPanel(question: String, nextEnabled: Boolean, vararg intermediate: JComponent) : JPanel() {
    val nextButton = NextButton(nextEnabled)

    init {
        layout = BorderLayout()
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        add(
            JPanel().apply {
                layout = FlowLayout(LEFT, 10, 10)
                add(question.label().bold())
                intermediate.forEach(::add)
                add(nextButton)
            }
        , NORTH)
        add(Box.createVerticalStrut(10))
    }
}
