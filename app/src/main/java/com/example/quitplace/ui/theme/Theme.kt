package com.example.quitplace.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// Quiet Corner - Dark Theme Color Scheme
// Calm, minimalist mental-health style with pastel lavender/blue accents
val DarkColorScheme = darkColorScheme(
    primary = LavenderAccent,                    // Основной лавандовый акцент
    onPrimary = Color(0xFF121212),                // Темный текст на лавандовом
    primaryContainer = Color(0xFF2A1F3D),       // Контейнер с лавандовым оттенком
    
    secondary = SoftBlueAccent,                 // Мягкий голубой для вторичных элементов
    onSecondary = Color(0xFF121212),
    secondaryContainer = Color(0xFF1F2832),     // Контейнер с голубым оттенком
    
    background = DarkBackground,                 // Основной темный фон
    onBackground = WarmOffWhite,                 // Теплый off-white основной текст
    
    surface = DarkSurface,                      // Поверхности карточек
    onSurface = WarmOffWhite,
    
    surfaceVariant = DarkSurfaceVariant,        // Вариант поверхности
    onSurfaceVariant = WarmOffWhiteVariant,     // Вторичный текст
    
    error = Color(0xFFCF6679),                  // Мягкий красный для ошибок
    onError = Color(0xFF121212),
    errorContainer = Color(0xFF3D1F1F),
    
    outline = Color(0xFF3A3A3A),                // Обводки
    outlineVariant = Color(0xFF2A2A2A)
)

// Дополнительные цвета для спокойной атмосферы
val CalmingBackground = LavenderGlow            // Лавандовое свечение
val CalmingBlueBackground = BlueGlow              // Голубое свечение