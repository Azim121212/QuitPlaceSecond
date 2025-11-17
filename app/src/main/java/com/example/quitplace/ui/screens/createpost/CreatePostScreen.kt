package com.example.quitplace.ui.screens.createpost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quitplace.domain.model.ProblemCategory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onBackClick: () -> Unit,
    onPostCreated: () -> Unit,
    viewModel: CreatePostViewModel = viewModel() // ← ИСПРАВЛЕНО: обычный viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showPreview by remember { mutableStateOf(false) }

    // Обработка успешной публикации
    LaunchedEffect(uiState.isPostCreated) {
        if (uiState.isPostCreated) {
            scope.launch {
                snackbarHostState.showSnackbar("✅ Пост успешно опубликован!")
                delay(1000)
                viewModel.resetPostCreated()
                onPostCreated()
            }
        }
    }

    // Обработка ошибок
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            scope.launch {
                snackbarHostState.showSnackbar("❌ $error")
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("✏️ Создать пост") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.resetState()
                        onBackClick()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "Назад")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PostTextField(
                text = uiState.text,
                onTextChange = { viewModel.onTextChange(it) },
                charCount = uiState.charCount,
                modifier = Modifier.fillMaxWidth()
            )

            QuickTemplates(
                onTemplateSelected = { viewModel.applyTemplate(it) },
                modifier = Modifier.fillMaxWidth()
            )

            CategorySelection(
                selectedCategory = uiState.category,
                onCategorySelected = { viewModel.selectCategory(it) },
                modifier = Modifier.fillMaxWidth()
            )

            TriggerWarningSection(
                isTriggerWarning = uiState.isTriggerWarning,
                onToggle = { viewModel.toggleTriggerWarning() },
                modifier = Modifier.fillMaxWidth()
            )

            ActionButtons(
                isValid = uiState.isValid,
                isSubmitting = uiState.isSubmitting,
                onPreviewClick = { showPreview = true },
                onSubmitClick = { viewModel.submit() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showPreview) {
        PreviewDialog(
            postText = uiState.text,
            category = uiState.category,
            isTriggerWarning = uiState.isTriggerWarning,
            onDismiss = { showPreview = false },
            onPublish = {
                showPreview = false
                viewModel.submit()
            }
        )
    }
}

// Остальные Composable функции остаются БЕЗ ИЗМЕНЕНИЙ...

@Composable
fun PostTextField(
    text: String,
    onTextChange: (String) -> Unit,
    charCount: Int,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val isError = charCount < 10 || charCount > 1000

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Текст поста",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = { Text("Поделитесь своими мыслями...") },
                isError = isError,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                singleLine = false,
                maxLines = 8
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isError) {
                    Text(
                        text = when {
                            charCount < 10 -> "Минимум 10 символов"
                            else -> "Максимум 1000 символов"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Box {}
                }

                Text(
                    text = "$charCount / 1000",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun QuickTemplates(
    onTemplateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val templates = listOf(
        "Сегодня чувствую сильную тревожность...",
        "Нужна поддержка в сложной ситуации...",
        "Хочу поделиться своим опытом преодоления...",
        "Не могу справиться с негативными мыслями...",
        "Ищу совета по поводу отношений..."
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Быстрые шаблоны",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                templates.forEach { template ->
                    FilterChip(
                        selected = false,
                        onClick = { onTemplateSelected(template) },
                        label = {
                            Text(
                                template,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CategorySelection(
    selectedCategory: ProblemCategory?,
    onCategorySelected: (ProblemCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Категория",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ProblemCategory.entries.take(4).forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        label = { Text(category.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ProblemCategory.entries.drop(4).forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { onCategorySelected(category) },
                        label = { Text(category.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }

            if (selectedCategory == null) {
                Text(
                    text = "Выберите категорию",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun TriggerWarningSection(
    isTriggerWarning: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Триггер-предупреждение",
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "Триггер-предупреждение",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Содержит чувствительный контент",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = isTriggerWarning,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
fun ActionButtons(
    isValid: Boolean,
    isSubmitting: Boolean,
    onPreviewClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onPreviewClick,
            modifier = Modifier.weight(1f),
            enabled = isValid && !isSubmitting
        ) {
            Icon(
                imageVector = Icons.Filled.Visibility,
                contentDescription = "Предпросмотр",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Предпросмотр")
        }

        Button(
            onClick = onSubmitClick,
            modifier = Modifier.weight(1f),
            enabled = isValid && !isSubmitting
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Опубликовать")
            }
        }
    }
}

@Composable
fun PreviewDialog(
    postText: String,
    category: ProblemCategory?,
    isTriggerWarning: Boolean,
    onDismiss: () -> Unit,
    onPublish: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Предпросмотр поста") },
        text = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    category?.let {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            Text("🎯 ${it.displayName}")
                        }
                    }

                    Text(
                        text = postText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (isTriggerWarning) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ) {
                            Text("⚠️ Триггер-предупреждение")
                        }
                    }

                    Text(
                        text = "Только что",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onPublish) {
                Text("Опубликовать")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Редактировать")
            }
        }
    )
}

@Preview
@Composable
fun CreatePostScreenPreview() {
    MaterialTheme {
        CreatePostScreen(
            onBackClick = {},
            onPostCreated = {}
        )
    }
}