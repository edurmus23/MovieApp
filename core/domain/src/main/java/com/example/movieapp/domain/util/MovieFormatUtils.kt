package com.example.movieapp.domain.util

object MovieFormatUtils {
    fun formatRuntime(runtimeMinutes: Int?): String {
        if (runtimeMinutes == null || runtimeMinutes <= 0) return "0 dk"
        
        val hours = runtimeMinutes / 60
        val minutes = runtimeMinutes % 60
        
        return if (hours > 0) {
            if (minutes > 0) {
                "${hours}sa ${minutes}dk"
            } else {
                "${hours}sa"
            }
        } else {
            "${minutes}dk"
        }
    }
}
