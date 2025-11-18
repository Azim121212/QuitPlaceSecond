package com.example.quitplace.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Используем обновленную цветовую схему из Theme.kt
// Dark theme first - основной режим приложения

@Composable
fun QuietCornerTheme(
    darkTheme: Boolean = true, // Всегда тёмная тема
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Quiet Corner использует темную тему по умолчанию
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme  // Используем обновленную схему из Theme.kt
        else -> DarkColorScheme       // Даже в светлой теме используем темную (dark theme first)
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        val currentWindow = (view.context as? Activity)?.window
            ?: throw Exception("Not in an activity - unable to get Window")
        currentWindow.statusBarColor = colorScheme.primary.toArgb()
        WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars = darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}