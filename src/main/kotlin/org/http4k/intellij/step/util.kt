package org.http4k.intellij.step

import java.awt.Component
import java.awt.Component.LEFT_ALIGNMENT
import java.awt.Font.BOLD
import java.awt.GridBagConstraints
import java.awt.GridBagConstraints.HORIZONTAL
import java.awt.GridBagConstraints.NORTH
import java.awt.GridBagConstraints.REMAINDER
import javax.swing.JComponent
import javax.swing.JLabel

fun String.label() = JLabel(this).apply {
    alignmentX = LEFT_ALIGNMENT
}

fun JLabel.bold() = apply {
    font = font.deriveFont(BOLD)
}

fun JLabel.header() = bold().apply {
    font = font.deriveFont(20f)
}

fun JComponent.addMax(that: Component) {
    add(that, GridBagConstraints().apply {
        fill = HORIZONTAL
        weightx = 1.0
        weighty = 1.0
        anchor = NORTH
        gridx = 0
        gridy = 0
        gridwidth = REMAINDER
    })
}
