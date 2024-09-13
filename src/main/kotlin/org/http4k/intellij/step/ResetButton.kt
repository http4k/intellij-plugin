package org.http4k.intellij.step

import javax.swing.JButton

fun ResetButton(onReset: OnReset) = JButton("Reset").apply { addActionListener { onReset() } }
