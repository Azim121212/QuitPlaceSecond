package com.example.quitplace.ui.screens.profile.screens.notifications

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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
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

data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false
)

enum class NotificationType {
    REPLY, SUPPORT, NEW_POST, SYSTEM
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBackClick: () -> Unit = {}) {
    // Временные данные с возможностью очистки
    val notifications = remember {
        mutableStateListOf(
            Notification(
                id = "1",
                type = NotificationType.REPLY,
                title = "Новый ответ на ваш пост",
                message = "Аноним оставил комментарий к вашему посту о тревожности",
                timestamp = "10 минут назад"
            ),
            Notification(
                id = "2",
                type = NotificationType.SUPPORT,
                title = "Кто-то поблагодарил за поддержку",
                message = "Ваш комментарий помог пользователю справиться с трудностями",
                timestamp = "2 часа назад"
            ),
            Notification(
                id = "3",
                type = NotificationType.NEW_POST,
                title = "Новый пост в категории 'Тревожность'",
                message = "Похожий на ваши интересы пост появился в ленте",
                timestamp = "1 день назад"
            ),
            Notification(
                id = "4",
                type = NotificationType.SYSTEM,
                title = "Добро пожаловать в Quiet Corner!",
                message = "Мы рады видеть вас в нашем сообществе поддержки",
                timestamp = "3 дня назад",
                isRead = true
            )
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("🔔 Уведомления") },
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
                },
                actions = {
                    if (notifications.isNotEmpty()) {
                        IconButton(
                            onClick = { notifications.clear() }
                        ) {
                            Text("Очистить")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (notifications.isEmpty()) {
            EmptyNotificationsState()
        } else {
            NotificationsList(
                notifications = notifications,
                onNotificationClick = { notification ->
                    // Помечаем как прочитанное
                    val index = notifications.indexOf(notification)
                    notifications[index] = notification.copy(isRead = true)
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun NotificationsList(
    notifications: List<Notification>,
    onNotificationClick: (Notification) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Кнопка очистки всех
        if (notifications.isNotEmpty()) {
            Button(
                onClick = { /* notifications.clear() будет в Scaffold */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Очистить все уведомления")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notifications) { notification ->
                NotificationCard(
                    notification = notification,
                    onClick = { onNotificationClick(notification) }
                )
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка типа уведомления
            Icon(
                imageVector = when (notification.type) {
                    NotificationType.REPLY -> Icons.Filled.Chat
                    NotificationType.SUPPORT -> Icons.Filled.Favorite
                    NotificationType.NEW_POST -> Icons.Filled.Notifications
                    NotificationType.SYSTEM -> Icons.Filled.Notifications
                },
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Контент уведомления
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
                Text(
                    text = notification.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Индикатор непрочитанного
            if (!notification.isRead) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Непрочитано",
                    modifier = Modifier.size(8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun EmptyNotificationsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.NotificationsOff,
            contentDescription = "Нет уведомлений",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Пока нет уведомлений",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Здесь появятся уведомления о новых ответах, поддержке и важных событиях",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview
@Composable
fun NotificationsScreenPreview() {
    MaterialTheme {
        NotificationsScreen()
    }
}