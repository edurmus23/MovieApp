package com.example.movieapp.data.local

import androidx.room.Room
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

actual val platformDatabaseModule = module {
    single {
        val dbFile = NSHomeDirectory() + "/movie_database.db"
        Room.databaseBuilder<MovieDatabase>(
            name = dbFile,
            factory = { MovieDatabase::class.instantiateImpl() }
        )
    }
}

// Room generates this for KMP
private fun <T> Any.instantiateImpl(): T = throw NotImplementedError()
