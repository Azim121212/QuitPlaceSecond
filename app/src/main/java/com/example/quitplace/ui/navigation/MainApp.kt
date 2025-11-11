package com.example.quitplace.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.quitplace.ui.screens.FeedScreen

// Главный компонент приложения - ТОЛЬКО лента
@Composable
fun MainApp() {
    FeedScreen()
}

@Preview
@Composable
fun MainAppPreview() {
    MainApp()
}