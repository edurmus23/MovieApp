package com.example.movieapp.core.ui.util

import android.content.res.Configuration
import android.os.LocaleList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
actual fun ProvideAppLocale(
    languageCode: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val targetLocale = remember(languageCode) {
        when (languageCode) {
            "tr" -> Locale("tr", "TR")
            "en" -> Locale("en", "US")
            else -> Locale.getDefault()
        }
    }

    val updatedContext = remember(languageCode) {
        Locale.setDefault(targetLocale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(targetLocale)
        config.setLocales(LocaleList(targetLocale))
        context.createConfigurationContext(config)
    }

    CompositionLocalProvider(
        LocalContext provides updatedContext,
        content = content
    )
}
