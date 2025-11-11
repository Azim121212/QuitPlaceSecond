package com.example.quitplace.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// Тёмная цветовая схема по твоему гайду
val DarkColorScheme = darkColorScheme(
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

// Дополнительные цвета
val CalmingBackground = Color(0x264ECDC4) // Синий с прозрачностью rgba(78, 205, 196, 0.15)