package com.example.quitplace.domain.repository

import com.example.quitplace.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun createPost(post: Post)
    suspend fun getPostById(id: String): Post
    fun getPosts(): Flow<List<Post>>
}