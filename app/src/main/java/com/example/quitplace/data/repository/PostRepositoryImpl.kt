package com.example.quitplace.data.repository

import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// In-memory реализация PostRepository
object PostRepositoryImpl : PostRepository {
    // Храним посты в памяти
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    private val posts: Flow<List<Post>> = _posts.asStateFlow()

    override suspend fun createPost(post: Post) {
        // Просто добавляем пост в начало списка
        val currentPosts = _posts.value.toMutableList()
        currentPosts.add(0, post) // Новые посты в начале
        _posts.value = currentPosts
    }

    override suspend fun getPostById(id: String): Post {
        return _posts.value.firstOrNull { it.id == id } ?: throw Exception("Post not found")
    }

    override fun getPosts(): Flow<List<Post>> {
        return posts
    }
}