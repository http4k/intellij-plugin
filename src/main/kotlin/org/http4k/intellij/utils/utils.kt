package org.http4k.intellij.utils

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.zip.ZipInputStream

fun InputStream.unzipInto(target: File) {
    ZipInputStream(this).use { stream ->
        var entry = stream.nextEntry
        while (entry != null) {
            val filePath = target.resolve(entry.name.substringAfter('/')).apply { parentFile.mkdirs() }
            FileOutputStream(filePath).use { fos ->
                BufferedOutputStream(fos).use { bos ->
                    stream.copyTo(bos, 4096)
                }
            }
            entry = stream.nextEntry
        }
    }
}
