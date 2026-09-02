package com.example.movieapp.core.ui.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.dataWithBytes
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile

@OptIn(ExperimentalForeignApi::class)
actual fun writeTempImageFile(bytes: ByteArray): String {
    val timestamp = (NSDate().timeIntervalSince1970 * 1000).toLong()
    val filePath = NSTemporaryDirectory() + "profile_temp_$timestamp.jpg"
    bytes.usePinned { pinned ->
        val nsData = NSData.dataWithBytes(pinned.addressOf(0), bytes.size.toULong())
        nsData.writeToFile(filePath, true)
    }
    return filePath
}
