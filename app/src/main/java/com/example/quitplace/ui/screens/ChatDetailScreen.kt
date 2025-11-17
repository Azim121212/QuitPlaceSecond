package com.example.quitplace.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// ИСПРАВЛЕННЫЙ ИМПОРТ:
import com.example.quitplace.data.repository.AIChatRepositoryImpl
import com.example.quitplace.domain.usecase.SendMessageToAIUseCase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chatName: String,
    onBackClick: () -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateOf(listOf<ChatMessage>()) }
    val listState = rememberLazyListState()

    // СОЗДАЁМ USE CASE
    val sendMessageUseCase = remember {
        SendMessageToAIUseCase(AIChatRepositoryImpl())
    }

    // Авто-скролл при новых сообщениях
    LaunchedEffect(messages.value.size) {
        if (messages.value.isNotEmpty()) {
            listState.animateScrollToItem(messages.value.size - 1)
        }
    }

    // Ответ AI на новые сообщения пользователя
    LaunchedEffect(messages.value) {
        val lastMessage = messages.value.lastOrNull()
        if (lastMessage != null && lastMessage.isUser) {
            // ИСПОЛЬЗУЕМ USE CASE ВМЕСТО ЗАГЛУШКИ
            val aiResponse = sendMessageUseCase(lastMessage.text)
            val aiMessage = ChatMessage(text = aiResponse, isUser = false)
            messages.value = messages.value + aiMessage
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Чат с $chatName",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Список сообщений
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.value) { message ->
                ChatMessageItem(message = message)
            }
        }

        // Поле ввода
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Напишите сообщение...") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        // Добавляем сообщение пользователя
                        val newMessage = ChatMessage(
                            text = messageText,
                            isUser = true
                        )
                        messages.value = messages.value + newMessage
                        messageText = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Отпр.")
            }
        }
    }
}

// Модель сообщения
data class ChatMessage(
    val text: String,
    val isUser: Boolean = true // true - пользователь, false - AI
)

// Компонент сообщения
@Composable
fun ChatMessageItem(message: ChatMessage) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Text(
            text = "${if (message.isUser) "Ты" else "Bimbo"}: ${message.text}",
            modifier = Modifier.padding(8.dp)
        )
    }
}