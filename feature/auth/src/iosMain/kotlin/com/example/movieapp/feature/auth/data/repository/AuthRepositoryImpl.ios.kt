package com.example.movieapp.feature.auth.data.repository

import dev.gitlive.firebase.storage.StorageReference
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual suspend fun uploadFileToFirebase(storageRef: StorageReference, uri: String): String {
    // TODO: Implement iOS file upload
    return ""
}

actual fun currentTimeMillis(): Long {
    return (NSDate().timeIntervalSince1970 * 1000).toLong()
}
