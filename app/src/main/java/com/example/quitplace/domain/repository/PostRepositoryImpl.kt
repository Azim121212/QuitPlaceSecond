package com.example.quitplace.data.repository

import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostRepositoryImpl : PostRepository {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())

    override suspend fun createPost(post: Post) {
        val currentPosts = _posts.value.toMutableList()
        currentPosts.add(0, post)
        _posts.value = currentPosts
    }

    override fun getPosts(): Flow<List<Post>> {
        return _posts.asStateFlow()
    }
}