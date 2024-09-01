package org.http4k.intellij.step

import com.intellij.ui.JBColor.GRAY
import org.http4k.intellij.wizard.Option
import javax.swing.BorderFactory.createCompoundBorder
import javax.swing.BorderFactory.createEmptyBorder
import javax.swing.BorderFactory.createLineBorder
import javax.swing.BoxLayout
import javax.swing.BoxLayout.X_AXIS
import javax.swing.BoxLayout.Y_AXIS
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextArea

fun OptionBox(control: JComponent, option: Option) = JPanel().apply {
    layout = BoxLayout(this, Y_AXIS)
    border = createCompoundBorder(createLineBorder(GRAY), createEmptyBorder(10, 10, 10, 10))

    add(JPanel().apply {
        layout = BoxLayout(this, X_AXIS)
        add(control)
        add(option.label.label().bold())
    })
    add(JTextArea(option.description).apply {
        lineWrap = true
        wrapStyleWord = true
        isOpaque = false
    })
}
