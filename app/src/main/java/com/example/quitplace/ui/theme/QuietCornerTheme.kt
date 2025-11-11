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

// Переименуем наши цветовые схемы чтобы избежать конфликта
private val QuietCornerDarkColorScheme = darkColorScheme(
    primary = Color(0xFF4ECDC4),        // Акцентный бирюзовый
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF3BAFA8),

    secondary = Color(0xFFA0AEC0),      // Второстепенный текст
    onSecondary = Color(0xFF000000),

    background = Color(0xFF1E2A3A),     // Основной фон
    onBackground = Color(0xFFE2E8F0),   // Основной текст

    surface = Color(0xFF2D3748),        // Поверхностные блоки
    onSurface = Color(0xFFE2E8F0),

    error = Color(0xFFE53E3E),          // Ошибки/тревожные элементы
    onError = Color(0xFFFFFFFF)
)

private val QuietCornerLightColorScheme = lightColorScheme(
    primary = Color(0xFF4ECDC4),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF3BAFA8),

    secondary = Color(0xFFA0AEC0),
    onSecondary = Color(0xFF000000),

    background = Color(0xFF1E2A3A),
    onBackground = Color(0xFFE2E8F0),

    surface = Color(0xFF2D3748),
    onSurface = Color(0xFFE2E8F0),

    error = Color(0xFFE53E3E),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun QuietCornerTheme(
    darkTheme: Boolean = true, // Всегда тёмная тема
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> QuietCornerDarkColorScheme
        else -> QuietCornerLightColorScheme
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