package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer
import org.http4k.intellij.wizard.Answer.Step
import org.http4k.intellij.wizard.Answer.Text
import java.awt.Color
import java.awt.Dimension
import javax.swing.BorderFactory.createCompoundBorder
import javax.swing.BorderFactory.createEmptyBorder
import javax.swing.BorderFactory.createLineBorder
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.JPanel

fun SummaryView(answers: List<Answer>) = JPanel().apply {
    layout = BoxLayout(this, Y_AXIS)
    border = createEmptyBorder(15, 15, 15, 15)
    add("http4k Project Wizard Summary".label().header())
    answers.filterIsInstance<Step>()
        .map(::toSectionPanel)
        .forEach(::add)
}

private fun flatten(answer: Answer): List<Answer> = listOf(answer) + when (answer) {
    is Step -> answer.answers.flatMap(::flatten) + answer.steps.flatMap(::flatten)
    is Text -> answer.steps.flatMap(::flatten)
}

private fun toSectionPanel(section: Answer) = JPanel().apply {
    layout = BoxLayout(this, Y_AXIS)
    maximumSize = Dimension(600, Int.MAX_VALUE)

    border = createCompoundBorder(
        createEmptyBorder(10, 10, 10, 10),
        createCompoundBorder(createLineBorder(Color.GRAY, 1), createEmptyBorder(20, 20, 20, 20))
    )
    add(section.label.label().header())
    add(Box.createVerticalStrut(10))

    section.steps.flatMap(::flatten)
        .forEach {
            when (it) {
                is Text -> if (it.answers.isNotEmpty()) {
                    add(JPanel().apply {
                        layout = BoxLayout(this, Y_AXIS)
                        add(it.label.label().bold())
                        add(Box.createVerticalStrut(3))
                        it.answers.map { it.label() }.forEach(::add)
                        add(Box.createVerticalStrut(10))
                    })
                }

                is Step -> {
                    add(
                        (when {
                            it.label.contains(".") -> it.label.substringBefore(".") + ":"
                            else -> it.label
                        }).label().bold()
                    )
                    add(Box.createVerticalStrut(3))
                    add((it.answers.map { it.label }.takeIf { it.isNotEmpty() }
                        ?: listOf("None")).joinToString(", ").label())
                    add(Box.createVerticalStrut(10))
                }
            }
        }
}
