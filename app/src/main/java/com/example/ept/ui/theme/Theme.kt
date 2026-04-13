package com.example.ept.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary          = Indigo600,
    onPrimary        = SurfaceLight,
    primaryContainer = Indigo50,
    onPrimaryContainer = Indigo700,
    secondary        = Green600,
    onSecondary      = SurfaceLight,
    secondaryContainer = Green100,
    onSecondaryContainer = Green700,
    error            = Red500,
    onError          = SurfaceLight,
    errorContainer   = Red100,
    background       = BackgroundLight,
    onBackground     = OnBackground,
    surface          = SurfaceLight,
    onSurface        = OnBackground,
    surfaceVariant   = Indigo50,
    onSurfaceVariant = Subtitle,
    outline          = Indigo100
)

@Composable
fun EPTTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme  // Using light theme only for professional HR feel

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
