package org.http4k.intellij.step

import javax.swing.JButton

class NextButton(enabledByDefault: Boolean = false) : JButton("Next") {
    init {
        addActionListener {
            isEnabled = enabledByDefault
        }
    }
}

