package com.example.movieapp.feature.auth.data.repository

import android.net.Uri
import dev.gitlive.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

actual suspend fun uploadFileToFirebase(storageRef: StorageReference, uri: String): String {
    val fileUri = Uri.parse(uri)
    val androidRef = storageRef.android
    androidRef.putFile(fileUri).await()
    return androidRef.downloadUrl.await().toString()
}

actual fun currentTimeMillis(): Long = System.currentTimeMillis()
