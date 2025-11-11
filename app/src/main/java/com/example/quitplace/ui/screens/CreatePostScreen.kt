package com.example.quitplace.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quitplace.ui.components.CategoryDropdown
import com.example.quitplace.ui.screens.createpost.CreatePostViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onBackClick: () -> Unit = {},
    onPostCreated: () -> Unit = {},
    viewModel: CreatePostViewModel = remember {
        CreatePostViewModel(
            com.example.quitplace.domain.usecase.CreatePostUseCase(
                com.example.quitplace.data.repository.PostRepositoryImpl
            )
        )
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        // Можно добавить side effects если нужно
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Создать пост",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            // Кнопка публикации
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Button(
                    onClick = {
                        viewModel.createPost(
                            onSuccess = onPostCreated,
                            onError = { error ->
                                errorMessage = error
                                showError = true
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.content.length >= 10) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        contentColor = if (uiState.content.length >= 10) Color.Black else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    ),
                    enabled = uiState.content.length >= 10,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (uiState.content.length >= 10) 8.dp else 2.dp
                    )
                ) {
                    Text(
                        text = if (uiState.content.length >= 10) "📤 ОПУБЛИКОВАТЬ" else "Заполните пост (мин. 10 символов)",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Выбор категории
            CategoryDropdown(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { category ->
                    viewModel.updateCategory(category)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Поле ввода текста
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "✍️ РАССКАЖИ, ЧТО ЧУВСТВУЕШЬ...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Поле ввода текста
                    BasicTextField(
                        value = uiState.content,
                        onValueChange = { text ->
                            if (text.length <= 1000) {
                                viewModel.updateContent(text)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        decorationBox = { innerTextField ->
                            if (uiState.content.isEmpty()) {
                                Text(
                                    text = "Опиши свои чувства подробнее...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Счётчик символов
                    Text(
                        text = "Осталось ${1000 - uiState.content.length} символов",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (uiState.content.length > 900) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        }
                    )
                }
            }

            // Быстрые шаблоны
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎭 БЫСТРЫЕ ШАБЛОНЫ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Сетка шаблонов 2 колонки
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickTemplates.chunked(2).forEach { rowTemplates ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowTemplates.forEach { template ->
                                    Button(
                                        onClick = {
                                            viewModel.updateContent(template)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(36.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                            contentColor = MaterialTheme.colorScheme.primary
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(
                                            defaultElevation = 2.dp
                                        )
                                    ) {
                                        Text(
                                            text = template.take(15) + "...",
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1
                                        )
                                    }
                                }

                                // Заполнитель если в ряду только один элемент
                                if (rowTemplates.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Настройки триггер-предупреждения
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "⚙️ НАСТРОЙКИ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Переключатель триггер-предупреждения
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Добавить предупреждение о триггере",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = uiState.showTriggerWarning,
                            onCheckedChange = { checked ->
                                viewModel.updateTriggerWarning(checked)
                            }
                        )
                    }

                    // Поле для текста предупреждения (показывается только если включено)
                    if (uiState.showTriggerWarning) {
                        Spacer(modifier = Modifier.height(12.dp))

                        BasicTextField(
                            value = uiState.triggerWarningText,
                            onValueChange = { text ->
                                if (text.length <= 100) {
                                    viewModel.updateTriggerWarning(true, text)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            textStyle = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            decorationBox = { innerTextField ->
                                if (uiState.triggerWarningText.isEmpty()) {
                                    Text(
                                        text = "Уточните предупреждение (необязательно)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Информация об анонимности
                    Text(
                        text = "💡 Твой пост будет полностью анонимным",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            // Отступ для кнопки публикации
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// Константа для быстрых шаблонов
val quickTemplates = listOf(
    "Чувствую постоянную тревогу...",
    "Не могу уснуть уже несколько ночей...",
    "Ощущаю сильное одиночество...",
    "Подавлен и нет сил...",
    "Стресс на работе не отпускает...",
    "Трудно общаться с людьми..."
)

@Preview
@Composable
fun CreatePostScreenPreview() {
    CreatePostScreen()
}