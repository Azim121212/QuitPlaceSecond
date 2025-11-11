package com.example.quitplace.domain.repository

import com.example.quitplace.domain.model.Post
import kotlinx.coroutines.flow.Flow

// Контракт для работы с постами
interface PostRepository {
    suspend fun createPost(post: Post)
    fun getPosts(): Flow<List<Post>>
}