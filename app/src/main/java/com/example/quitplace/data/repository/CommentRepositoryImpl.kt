package com.example.quitplace.data.repository

import com.example.quitplace.domain.model.Comment
import com.example.quitplace.domain.repository.CommentRepository

class CommentRepositoryImpl : CommentRepository {
    private val comments = mutableListOf<Comment>()

    override suspend fun getCommentsByPostId(postId: String): List<Comment> {
        return comments.filter { it.postId == postId }
    }

    override suspend fun addComment(comment: Comment) {
        comments.add(comment)
    }
}