package com.example.movieapp.feature.auth.data.local.util

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
    private val application: Application
) {
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_USERNAME = stringPreferencesKey("user_username")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_IMAGE_URL = stringPreferencesKey("user_image_url")
    }

    val userId: Flow<String?> = application.dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    val userName: Flow<String?> = application.dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val userUsername: Flow<String?> = application.dataStore.data.map { preferences ->
        preferences[USER_USERNAME]
    }

    val userImageUrl: Flow<String?> = application.dataStore.data.map { preferences ->
        preferences[USER_IMAGE_URL]?.takeIf { it.isNotBlank() }
    }

    suspend fun saveSession(id: String, name: String, email: String, username: String? = null, imageUrl: String? = null) {
        application.dataStore.edit { preferences ->
            preferences[USER_ID] = id
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
            username?.let { preferences[USER_USERNAME] = it }
            if (imageUrl.isNullOrBlank()) {
                preferences.remove(USER_IMAGE_URL)
            } else {
                preferences[USER_IMAGE_URL] = imageUrl
            }
        }
    }

    suspend fun clearSession() {
        application.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}
