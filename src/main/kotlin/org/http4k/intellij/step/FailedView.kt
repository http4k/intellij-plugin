package org.http4k.intellij.step

import com.intellij.ui.JBColor.DARK_GRAY
import com.intellij.ui.JBColor.GRAY
import org.http4k.cloudnative.RemoteRequestFailed
import org.jdesktop.swingx.JXHyperlink
import java.awt.Desktop
import java.awt.GridBagConstraints
import java.awt.GridBagConstraints.NONE
import java.awt.GridBagConstraints.NORTH
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.net.URI
import javax.swing.AbstractAction
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.TitledBorder

fun FailedView(error: RemoteRequestFailed) = JPanel(GridBagLayout()).apply {
    border = BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(GRAY, 1), "    http4k Toolbox Project Wizard    ",
            TitledBorder.CENTER,
            TitledBorder.DEFAULT_POSITION,
            font.deriveFont(20f)
        ),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)
    )

    add(JLabel("The http4k toolbox returned ${error.status}").apply {
        font = font.deriveFont(14f)
    },
        GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            weightx = 1.0
            weighty = 0.0
            anchor = NORTH
            fill = NONE
        })
    addMax(
        JPanel(GridBagLayout()).apply {
            border = BorderFactory.createEmptyBorder(40, 40, 40, 40)
            addMax(JXHyperlink(object : AbstractAction("Click here to generate a project using the Toolbox site") {
                override fun actionPerformed(e: ActionEvent) =
                    Desktop.getDesktop().browse(URI("https://toolbox.http4k.org/project"))
            }).apply {
                setUnclickedColor(GRAY);
                setClickedColor(DARK_GRAY);
                horizontalAlignment = JLabel.CENTER
            })
        },
    )
}
