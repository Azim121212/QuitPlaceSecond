package com.example.quitplace.domain.usecase

import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow


class GetPostsUseCase (
    private val postRepository: PostRepository
) {
    operator fun invoke(): Flow<List<Post>> {
        return postRepository.getPosts()
    }
}