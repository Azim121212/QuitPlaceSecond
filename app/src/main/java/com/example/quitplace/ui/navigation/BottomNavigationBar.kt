package com.example.quitplace.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

// Экраны нашего приложения
sealed class Screen(
    val route: String,
    val title: String,
    val iconRes: Int // Пока заглушка, потом добавим иконки
) {
    object Feed : Screen("feed", "Лента", 0)
    object Chat : Screen("chat", "Чат", 0)
    object Profile : Screen("profile", "Профиль", 0)
}

// Bottom Navigation Bar
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
                icon = {
                    Icon(
                        // Пока заглушка для иконки
                        painter = painterResource(id = android.R.drawable.ic_menu_help),
                        contentDescription = screen.title
                    )
                },
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