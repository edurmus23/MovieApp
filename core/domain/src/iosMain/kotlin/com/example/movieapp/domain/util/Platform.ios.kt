package com.example.movieapp.domain.util

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun getCountryCode(): String {
    return NSLocale.currentLocale.countryCode ?: "US"
}
