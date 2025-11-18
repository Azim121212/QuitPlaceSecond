package com.example.quitplace.domain.model

data class Comment(
    val id: String,
    val postId: String,
    val username: String,
    val text: String,
    val timestamp: Long
)