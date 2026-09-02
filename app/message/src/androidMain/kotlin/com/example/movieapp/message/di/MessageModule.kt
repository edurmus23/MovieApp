package com.example.movieapp.message.di

import com.example.movieapp.message.util.NotificationHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val messageModule = module {
    single { NotificationHelper(androidContext()) }
}
