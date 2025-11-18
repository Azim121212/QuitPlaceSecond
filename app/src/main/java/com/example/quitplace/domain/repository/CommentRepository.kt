package com.example.quitplace.domain.repository

import com.example.quitplace.domain.model.Comment

interface CommentRepository {
    suspend fun getCommentsByPostId(postId: String): List<Comment>
    suspend fun addComment(comment: Comment)
}