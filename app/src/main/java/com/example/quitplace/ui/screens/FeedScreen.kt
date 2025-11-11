package com.example.quitplace.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quitplace.domain.model.ProblemCategory
import com.example.quitplace.ui.components.PostCard
import com.example.quitplace.ui.screens.feed.FeedViewModel

@Composable
fun FeedScreen(
    onCreatePostClick: () -> Unit = {},
    viewModel: FeedViewModel = remember {
        FeedViewModel(
            com.example.quitplace.domain.usecase.GetPostsUseCase(
                com.example.quitplace.data.repository.PostRepositoryImpl
            )
        )
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Верхняя панель с фильтром
            FeedTopAppBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = { query ->
                    viewModel.updateSearchQuery(query)
                },
                selectedFilter = uiState.selectedCategory,
                onFilterClick = { showFilterMenu = true }
            )

            // Выпадающее меню фильтров
            DropdownMenu(
                expanded = showFilterMenu,
                onDismissRequest = { showFilterMenu = false },
                modifier = Modifier
                    .width(200.dp)
            ) {
                // Опция "Все посты"
                DropdownMenuItem(
                    text = { Text("Все посты") },
                    onClick = {
                        viewModel.selectCategory(null)
                        showFilterMenu = false
                    }
                )

                // Разделитель
                androidx.compose.material3.Divider()

                // Категории
                ProblemCategory.entries.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.displayName) },
                        onClick = {
                            viewModel.selectCategory(category)
                            showFilterMenu = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Лента постов
            val filteredPosts = if (uiState.selectedCategory == null) {
                uiState.posts // Все посты
            } else {
                uiState.posts.filter { it.category == uiState.selectedCategory }
            }

            if (filteredPosts.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val currentCategory = uiState.selectedCategory
                    val message = if (currentCategory == null) {
                        "Пока нет постов. Будь первым!"
                    } else {
                        "Нет постов в категории ${currentCategory.displayName}"
                    }
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredPosts) { post ->
                        PostCard(post = post)
                    }
                }
            }
        }

        // FAB кнопка
        FloatingActionButton(
            onClick = onCreatePostClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.Black
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Создать пост"
            )
        }
    }
}

@Composable
private fun FeedTopAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: ProblemCategory?,
    onFilterClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Кнопка фильтра
        TextButton(
            onClick = onFilterClick,
            modifier = Modifier.height(48.dp)
        ) {
            Text(
                text = "Фильтр",
                color = if (selectedFilter != null) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Логотип
        Text(
            text = "🌙 Quiet Corner",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )

        // Кнопка поиска
        IconButton(
            onClick = { /* TODO: Реализовать поиск */ }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun FeedScreenPreview() {
    FeedScreen()
}