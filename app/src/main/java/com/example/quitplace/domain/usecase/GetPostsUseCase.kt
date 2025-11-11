package com.example.quitplace.domain.usecase

import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// UseCase для получения постов
class GetPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(): Flow<List<Post>> {
        return postRepository.getPosts()
    }
}