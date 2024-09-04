package org.http4k.intellij.step

import com.intellij.ui.JBColor.GRAY
import org.http4k.intellij.wizard.Option
import java.awt.Dimension
import javax.swing.BorderFactory.createCompoundBorder
import javax.swing.BorderFactory.createEmptyBorder
import javax.swing.BorderFactory.createLineBorder
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.BoxLayout.X_AXIS
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextArea

fun OptionBox(control: JComponent, option: Option) = JPanel().apply {
    layout = BoxLayout(this, X_AXIS)
    border = createCompoundBorder(createLineBorder(GRAY), createEmptyBorder(10, 10, 10, 10))

    add(JPanel().apply {
        layout = BoxLayout(this, X_AXIS)
        preferredSize = Dimension(175, 0)
        add(control)
        add(option.label.label().bold())
    })
    add(Box.createHorizontalStrut(10))
    add(JTextArea(option.description).apply {
        border = createEmptyBorder()
        lineWrap = true
        wrapStyleWord = true
        isOpaque = false
    })
}
