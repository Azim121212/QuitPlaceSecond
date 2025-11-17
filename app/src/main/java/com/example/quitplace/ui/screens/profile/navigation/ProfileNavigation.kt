package com.example.quitplace.ui.screens.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

// Profile navigation routes
const val PROFILE_NAV_GRAPH = "profile_nav_graph"
const val PROFILE_ROUTE = "profile"

const val MY_POSTS_ROUTE = "profile/posts"
const val MY_COMMENTS_ROUTE = "profile/comments"
const val FAVORITES_ROUTE = "profile/favorites"
const val NOTIFICATIONS_ROUTE = "profile/notifications"
const val PRIVACY_ROUTE = "profile/privacy"
const val EMERGENCY_ROUTE = "profile/emergency"
const val HELP_ROUTE = "profile/help"
const val EDIT_PROFILE_ROUTE = "profile/edit"
const val SETTINGS_ROUTE = "profile/settings"

fun NavGraphBuilder.profileNavGraph(
    navController: NavController,
    onBackClick: () -> Unit
) {
    navigation(
        startDestination = PROFILE_ROUTE,
        route = PROFILE_NAV_GRAPH
    ) {
        // Main profile screen will be added here later
        // We'll add composable destinations for each screen
    }
}