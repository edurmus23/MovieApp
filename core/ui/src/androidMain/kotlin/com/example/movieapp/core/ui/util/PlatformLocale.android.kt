package com.example.movieapp.core.ui.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

actual fun changeAppLanguage(languageCode: String) {
    val localeList = if (languageCode == "system") {
        LocaleListCompat.getEmptyLocaleList()
    } else {
        LocaleListCompat.forLanguageTags(languageCode)
    }
    AppCompatDelegate.setApplicationLocales(localeList)
}
