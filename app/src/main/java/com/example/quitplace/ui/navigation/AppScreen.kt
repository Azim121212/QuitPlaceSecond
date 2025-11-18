package com.example.quitplace.ui.navigation

sealed class AppScreen(val route: String) {
    object Feed : AppScreen("feed")
    object CreatePost : AppScreen("create_post")
    data class PostDetails(val postId: String) : AppScreen("post_details/{postId}") {
        fun createRoute(postId: String) = "post_details/$postId"
    }
}