package com.example.quitplace.domain.usecase

import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.repository.PostRepository
import javax.inject.Inject

// UseCase для создания поста
class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(post: Post) {
        // Валидация поста
        if (post.content.isBlank()) {
            throw IllegalArgumentException("Пост не может быть пустым")
        }
        if (post.content.length < 10) {
            throw IllegalArgumentException("Пост должен содержать минимум 10 символов")
        }

        // Сохраняем пост
        postRepository.createPost(post)
    }
}