package com.example.quitplace.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quitplace.ui.navigation.AppScreen

// Data Classes
data class ProfileState(
    val username: String = "Аноним #1234",
    val supportLevel: String = "Начинающий",
    val progress: Float = 0.3f,
    val messagesSent: Int = 0,
    val helpedCount: Int = 0,
    val postsCount: Int = 0
)

// Main Screen - БЕЗ ViewModel пока
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigationClick: (AppScreen) -> Unit = {}  // ДОБАВЛЕН ПАРАМЕТР
) {
    // Временное состояние вместо ViewModel
    val uiState = remember {
        ProfileState(
            username = "Аноним #1234",
            supportLevel = "Начинающий",
            progress = 0.3f,
            messagesSent = 0,
            helpedCount = 0,
            postsCount = 0
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header Section
        ProfileHeader(
            username = uiState.username,
            supportLevel = uiState.supportLevel,
            progress = uiState.progress
        )

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileStat(
                icon = Icons.Outlined.Chat,
                count = uiState.messagesSent,
                label = "Сообщений",
                modifier = Modifier.weight(1f)
            )
            ProfileStat(
                icon = Icons.Filled.Favorite,
                count = uiState.helpedCount,
                label = "Помог другим",
                modifier = Modifier.weight(1f)
            )
            ProfileStat(
                icon = Icons.Filled.Person,
                count = uiState.postsCount,
                label = "Постов",
                modifier = Modifier.weight(1f)
            )
        }

        // Action List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(300.dp)
        ) {
            item {
                ProfileActionItem(
                    icon = Icons.Outlined.Edit,
                    text = "Мои посты",
                    onClick = { onNavigationClick(AppScreen.MyPosts) }
                )
            }
            item {
                ProfileActionItem(
                    icon = Icons.Outlined.Chat,
                    text = "Мои комментарии",
                    onClick = { onNavigationClick(AppScreen.MyComments) }
                )
            }
            item {
                ProfileActionItem(
                    icon = Icons.Filled.Favorite,
                    text = "Избранное",
                    onClick = { onNavigationClick(AppScreen.Favorites) }
                )
            }
            item {
                ProfileActionItem(
                    icon = Icons.Filled.Notifications,
                    text = "Уведомления",
                    onClick = { onNavigationClick(AppScreen.Notifications) }
                )
            }
            item {
                ProfileActionItem(
                    icon = Icons.Filled.Security,
                    text = "Конфиденциальность",
                    onClick = { onNavigationClick(AppScreen.Privacy) }
                )
            }
            item {
                ProfileActionItem(
                    icon = Icons.Outlined.Warning,
                    text = "Экстренная помощь",
                    onClick = { onNavigationClick(AppScreen.Emergency) }
                )
            }
            item {
                ProfileActionItem(
                    icon = Icons.Outlined.Help,
                    text = "Помощь и поддержка",
                    onClick = { onNavigationClick(AppScreen.Help) }
                )
            }
        }

        // Bottom Section
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { onNavigationClick(AppScreen.EditProfile) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Редактировать профиль")
            }

            OutlinedButton(
                onClick = { onNavigationClick(AppScreen.Settings) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Настройки")
            }

            Text(
                text = "Версия 1.0.0",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

// Header Component
@Composable
fun ProfileHeader(
    username: String,
    supportLevel: String,
    progress: Float
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Аватар",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // User Info
                Column {
                    Text(
                        text = username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = supportLevel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                )
                Text(
                    text = "Прогресс поддержки ${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Stat Component
@Composable
fun ProfileStat(
    icon: ImageVector,
    count: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Action Item Component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileActionItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Previews
@Preview(showBackground = true, name = "Light Theme")
@Composable
fun ProfileScreenPreviewLight() {
    MaterialTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun ProfileScreenPreviewDark() {
    MaterialTheme {
        ProfileScreen()
    }
}