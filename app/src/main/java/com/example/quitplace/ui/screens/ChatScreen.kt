package com.example.quitplace.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onChatClick: (String) -> Unit = {} // Теперь работает!
) {
    var searchQuery by remember { mutableStateOf("") }

    val chatList = listOf(
        ChatItem("Bimbo", "AI-психолог", "🧠"),
        ChatItem("Аноним 1", "Ищет поддержку", "👤"),
        ChatItem("Аноним 2", "Прошёл депрессию", "👤"),
        ChatItem("Аноним 3", "Специалист по тревожности", "👤")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Поиск чатов...") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            items(chatList) { chat ->
                ChatListItem(
                    chat = chat,
                    onClick = { onChatClick(chat.name) }
                )
            }
        }
    }
}

data class ChatItem(
    val name: String,
    val status: String,
    val emoji: String
)

@Composable
fun ChatListItem(
    chat: ChatItem,
    onClick: () -> Unit
) {
    // ЗАМЕНА: Text на кликабельный Surface
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "${chat.emoji} ${chat.name} - ${chat.status}",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    ChatScreen()
}