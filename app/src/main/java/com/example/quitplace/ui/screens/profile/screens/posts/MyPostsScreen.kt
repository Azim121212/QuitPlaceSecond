package com.example.quitplace.ui.screens.profile.screens.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class UserPost(
    val id: String,
    val content: String,
    val category: String,
    val timestamp: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(onBackClick: () -> Unit = {}) {
    // Временные данные
    val posts = listOf(
        UserPost(
            id = "1",
            content = "Сегодня чувствую сильную тревожность. Не могу сосредоточиться на работе.",
            category = "Тревожность",
            timestamp = "2 часа назад"
        ),
        UserPost(
            id = "2",
            content = "Поделюсь своим опытом борьбы с бессонницей. Мне помогли вечерние прогулки.",
            category = "Сон",
            timestamp = "1 день назад"
        ),
        UserPost(
            id = "3",
            content = "Спасибо всем за поддержку в трудный период. Стало немного легче.",
            category = "Благодарность",
            timestamp = "3 дня назад"
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("📝 Мои посты") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (posts.isEmpty()) {
            EmptyPostsState()
        } else {
            PostsList(
                posts = posts,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun PostsList(
    posts: List<UserPost>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            PostCard(post = post)
        }
    }
}

@Composable
fun PostCard(post: UserPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Категория
            Text(
                text = "🎯 ${post.category}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            // Текст поста
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3
            )

            // Время и действия
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { /* TODO: Редактировать */ }
                    ) {
                        Text("Редактировать")
                    }
                    Button(
                        onClick = { /* TODO: Удалить */ }
                    ) {
                        Text("Удалить")
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyPostsState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "📝",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "Вы пока не создали ни одного поста",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = { /* TODO: Создать первый пост */ }) {
                Text("Создать первый пост")
            }
        }
    }
}

@Preview
@Composable
fun MyPostsScreenPreview() {
    MaterialTheme {
        MyPostsScreen()
    }
}