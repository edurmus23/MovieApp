package com.example.movieapp.core.ui.util

import java.io.File

actual fun writeTempImageFile(bytes: ByteArray): String {
    val tempFile = File.createTempFile("profile_temp_", ".jpg")
    tempFile.writeBytes(bytes)
    return tempFile.absolutePath
}
