package com.example.movieapp.domain.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun formatJoinDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
