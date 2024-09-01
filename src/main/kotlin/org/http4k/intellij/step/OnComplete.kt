package org.http4k.intellij.step

import org.http4k.intellij.wizard.Answer

fun interface OnComplete : (List<Answer>) -> Unit
