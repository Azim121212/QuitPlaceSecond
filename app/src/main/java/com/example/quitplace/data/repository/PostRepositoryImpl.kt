package com.example.quitplace.data.repository

import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostRepositoryImpl private constructor() : PostRepository {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    
    companion object {
        @Volatile
        private var INSTANCE: PostRepositoryImpl? = null
        
        fun getInstance(): PostRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PostRepositoryImpl().also { INSTANCE = it }
            }
        }
    }

    override suspend fun createPost(post: Post) {
        val current = _posts.value.toMutableList()
        current.add(0, post)
        _posts.value = current
    }

    override suspend fun getPostById(id: String): Post {
        return _posts.value.firstOrNull { it.id == id } ?: throw Exception("Post not found")
    }

    override fun getPosts(): Flow<List<Post>> {
        return _posts.asStateFlow()
    }
}