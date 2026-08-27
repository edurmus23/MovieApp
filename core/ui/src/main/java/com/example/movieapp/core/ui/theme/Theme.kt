package com.example.movieapp.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// IMDb Dark Teması (Full Siyah - OLED Friendly)
private val DarkColorScheme = darkColorScheme(
    primary = ImdbYellow,
    onPrimary = Color.Black,
    secondary = ImdbYellow,
    onSecondary = Color.White,
    background = ImdbBlack,
    onBackground = Color.White,
    surface = ImdbBlack,
    onSurface = Color.White,
    surfaceVariant = ImdbBlack,
    onSurfaceVariant = ImdbLightGrey,
    primaryContainer = ImdbYellow.copy(alpha = 0.2f),
    onPrimaryContainer = ImdbYellow,
    secondaryContainer = ImdbYellow.copy(alpha = 0.2f),
    onSecondaryContainer = ImdbYellow
)

// IMDb Light Teması (Gündüz modu için sarı-beyaz uyumu)
private val LightColorScheme = lightColorScheme(
    primary = ImdbYellow,
    onPrimary = Color.Black,
    secondary = ImdbBlue,
    background = Color.White,
    onBackground = ImdbBlack,
    surface = Color.White,
    onSurface = ImdbBlack,
    surfaceVariant = Color(0xFFF2F2F2),
    onSurfaceVariant = Color.DarkGray,
    primaryContainer = ImdbYellow.copy(alpha = 0.2f),
    onPrimaryContainer = Color.Black,
    secondaryContainer = ImdbYellow.copy(alpha = 0.2f),
    onSecondaryContainer = Color.Black
)

@Composable
fun MovieAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), 
    // IMDb stili için dinamik rengi varsayılan olarak kapattık
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
