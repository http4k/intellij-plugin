package org.http4k.intellij.step

import java.awt.Component.LEFT_ALIGNMENT
import java.awt.Font.BOLD
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
