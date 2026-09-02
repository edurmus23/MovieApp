package com.example.movieapp.feature.auth.di

import com.example.movieapp.domain.repository.AuthRepository
import com.example.movieapp.feature.auth.data.repository.AuthRepositoryImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import org.koin.dsl.module

val authModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    single { Firebase.storage }
    single<AuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get(),
            sessionManager = get(),
            database = get(),
            firestore = get(),
            storage = get()
        )
    }
}
