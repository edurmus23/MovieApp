package com.example.movieapp.domain.util

import java.util.Locale

actual fun getCountryCode(): String {
    return Locale.getDefault().country.uppercase()
}
