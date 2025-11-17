package com.example.quitplace.ui.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onItemSelected: (Screen) -> Unit
) {
    val items = listOf(
        Screen.Feed,
        Screen.Chat,
        Screen.Profile
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Text(screen.title.take(1)) }, // Просто первая буква названия
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = { onItemSelected(screen) }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        currentRoute = Screen.Feed.route,
        onItemSelected = {}
    )
}