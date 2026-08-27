package com.example.movieapp.message.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "movie_app_notifications"
        private const val CHANNEL_NAME = "Movie App Notifications"
        private const val CHANNEL_DESCRIPTION = "Notifications for movie updates"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        title: String?,
        body: String?,
        movieId: String? = null,
        posterUrl: String? = null
    ) {
        // Run in background scope for consistency, even if we don't do heavy networking now
        CoroutineScope(Dispatchers.IO).launch {
            // 1. Create the Deep Link Intent
            val intent = if (!movieId.isNullOrBlank()) {
                android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse("movieapp://movie/$movieId")
                ).apply {
                    `package` = context.packageName
                    putExtra("movie_id", movieId) // Hem URI hem extra olarak ekle
                    // Note: If we had list deep links, we would add list_id and user_id here too
                    // Add flags to ensure the activity is started correctly from a notification
                    addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
            } else {
                context.packageManager.getLaunchIntentForPackage(context.packageName)
            }

            val pendingIntent = PendingIntent.getActivity(
                context, 
                System.currentTimeMillis().toInt(), // Unique request code to avoid intent recycling
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title ?: "MovieApp")
                .setContentText(body ?: "")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            // Optional: Still handle poster if provided, but primary goal is navigation
            if (!posterUrl.isNullOrBlank()) {
                try {
                    val url = URL(posterUrl)
                    val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    if (bitmap != null) {
                        builder.setLargeIcon(bitmap)
                        builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null as Bitmap?))
                    }
                } catch (e: Exception) {
                    android.util.Log.e("FCM", "Poster download failed, showing text only: ${e.message}")
                }
            }

            withContext(Dispatchers.Main) {
                notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
            }
        }
    }
}
