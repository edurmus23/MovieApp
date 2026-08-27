package com.example.movieapp

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        
        FirebaseMessaging.getInstance().subscribeToTopic("new_releases")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "new_releases konusuna abone olundu")
                } else {
                    Log.e("FCM", "Abonelik hatası: ${task.exception?.message}")
                }
            }

        FirebaseMessaging.getInstance().subscribeToTopic("recommendations")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "recommendations konusuna abone olundu")
                }
            }
    }
}
