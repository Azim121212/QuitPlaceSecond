package com.example.quitplace.ui.screens.profile.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class FavoritePost(
    val id: String,
    val content: String,
    val category: String,
    val author: String,
    val isFavorite: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(onBackClick: () -> Unit = {}) {
    // Временные данные с возможностью удаления из избранного
    val favoritePosts = remember {
        mutableStateListOf(
            FavoritePost(
                id = "1",
                content = "Техника 5-4-3-2-1 для борьбы с тревогой: назови 5 вещей которые видишь, 4 которые ощущаешь...",
                category = "Тревожность",
                author = "Аноним"
            ),
            FavoritePost(
                id = "2",
                content = "Как я научился справляться с паническими атаками. Делитесь своими методами!",
                category = "Панические атаки",
                author = "Аноним"
            ),
            FavoritePost(
                id = "3",
                content = "Ежедневные ритуалы для поддержания ментального здоровья. Что работает для вас?",
                category = "Самопомощь",
                author = "Аноним"
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("⭐ Избранное") },
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
        if (favoritePosts.isEmpty()) {
            EmptyFavoritesState()
        } else {
            FavoritesList(
                posts = favoritePosts,
                onRemoveFavorite = { post -> favoritePosts.remove(post) },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun FavoritesList(
    posts: List<FavoritePost>,
    onRemoveFavorite: (FavoritePost) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            FavoritePostCard(
                post = post,
                onRemoveFavorite = { onRemoveFavorite(post) }
            )
        }
    }
}

@Composable
fun FavoritePostCard(
    post: FavoritePost,
    onRemoveFavorite: () -> Unit
) {
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
            // Категория и автор
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎯 ${post.category}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "от ${post.author}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Текст поста
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3
            )

            // Кнопка удаления из избранного
            IconButton(
                onClick = onRemoveFavorite,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Удалить из избранного",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyFavoritesState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Нет избранного",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Нет избранных постов",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Добавляйте посты в избранное, чтобы вернуться к ним позже",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = { /* TODO: Перейти в ленту */ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Найти интересные посты")
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    MaterialTheme {
        FavoritesScreen()
    }
}