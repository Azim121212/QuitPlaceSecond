package com.example.quitplace.ui.screens.profile.screens.emergency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri

data class EmergencyContact(
    val id: String,
    val name: String,
    val phone: String,
    val website: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(onBackClick: () -> Unit = {}) {
    val context = LocalContext.current

    val emergencyContacts = listOf(
        EmergencyContact(
            id = "1",
            name = "Телефон доверия",
            phone = "8-800-2000-122",
            website = "https://telefon-doveria.ru",
            description = "Бесплатная психологическая помощь круглосуточно"
        ),
        EmergencyContact(
            id = "2",
            name = "Кризисный центр для женщин",
            phone = "8-800-7000-600",
            website = "https://anna-center.ru",
            description = "Помощь женщинам в трудной жизненной ситуации"
        ),
        EmergencyContact(
            id = "3",
            name = "Фонд борьбы с депрессией",
            phone = "8-800-333-44-34",
            website = "https://depressii.net",
            description = "Профилактика и помощь при депрессивных состояниях"
        ),
        EmergencyContact(
            id = "4",
            name = "Анонимные наркологи",
            phone = "8-800-100-01-02",
            website = "https://narcology.ru",
            description = "Консультации по зависимостям и реабилитации"
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("🆘 Экстренная помощь") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    titleContentColor = MaterialTheme.colorScheme.onErrorContainer
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Важное предупреждение
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Важно",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Column {
                        Text(
                            text = "Если вам нужна срочная помощь",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Не стесняйтесь обращаться к профессионалам",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            // Контакты экстренных служб
            Text(
                text = "Экстренные службы",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            emergencyContacts.forEach { contact ->
                EmergencyContactCard(
                    contact = contact,
                    onCallClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${contact.phone}")
                        }
                        context.startActivity(intent)
                    },
                    onWebsiteClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(contact.website)
                        }
                        context.startActivity(intent)
                    }
                )
            }

            // Важная информация
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Важная информация",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "• Все службы работают анонимно и бесплатно\n" +
                                "• Вы можете обратиться в любое время суток\n" +
                                "• Специалисты прошли профессиональную подготовку\n" +
                                "• Ваш разговор останется конфиденциальным",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Дополнительные ресурсы
            Text(
                text = "Помните: обращение за помощью - это проявление силы, а не слабости.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun EmergencyContactCard(
    contact: EmergencyContact,
    onCallClick: () -> Unit,
    onWebsiteClick: () -> Unit
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
            // Название и описание
            Column {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = contact.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Телефон
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = "Телефон",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = contact.phone,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            // Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onCallClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = "Позвонить",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Позвонить")
                }

                OutlinedButton(
                    onClick = onWebsiteClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = "Сайт",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Сайт")
                }
            }
        }
    }
}

@Preview
@Composable
fun EmergencyScreenPreview() {
    MaterialTheme {
        EmergencyScreen()
    }
}