package com.example.quitplace.ui.navigation

import androidx.compose.material3.MaterialTheme
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

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { 
                    Text(
                        text = screen.title.take(1),
                        style = MaterialTheme.typography.titleMedium,
                        color = if (currentRoute == screen.route) 
                            MaterialTheme.colorScheme.primary  // Лавандовый акцент при выборе
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = { 
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Normal  // Спокойный
                    )
                },
                selected = currentRoute == screen.route,
                onClick = { onItemSelected(screen) },
                colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,  // Лавандовый акцент
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer  // Лавандовый контейнер
                )
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