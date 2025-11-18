package com.example.quitplace.ui.navigation

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.quitplace.ui.screens.ChatDetailScreen
import com.example.quitplace.ui.screens.ChatScreen
import com.example.quitplace.ui.screens.createpost.CreatePostScreen
import com.example.quitplace.ui.screens.feed.FeedScreen
import com.example.quitplace.ui.screens.ProfileScreen
import com.example.quitplace.ui.screens.postdetails.PostDetailsScreen
import com.example.quitplace.ui.screens.postdetails.PostDetailsViewModel
import com.example.quitplace.ui.screens.profile.screens.posts.MyPostsScreen
import com.example.quitplace.ui.screens.profile.screens.comments.MyCommentsScreen
import com.example.quitplace.ui.screens.profile.screens.favorites.FavoritesScreen
import com.example.quitplace.ui.screens.profile.screens.notifications.NotificationsScreen
import com.example.quitplace.ui.screens.profile.screens.privacy.PrivacyScreen
import com.example.quitplace.ui.screens.profile.screens.emergency.EmergencyScreen
import com.example.quitplace.ui.screens.profile.screens.help.HelpScreen
import com.example.quitplace.ui.screens.profile.screens.edit.EditProfileScreen
import com.example.quitplace.ui.screens.profile.screens.settings.SettingsScreen
import com.example.quitplace.data.repository.PostRepositoryImpl
import com.example.quitplace.data.repository.CommentRepositoryImpl

// Состояния навигации
sealed class AppScreen {
    object Main : AppScreen()
    object CreatePost : AppScreen()
    data class ChatDetail(val chatName: String) : AppScreen()
    data class PostDetails(val postId: String) : AppScreen()

    // ПРОФИЛЬНЫЕ ЭКРАНЫ
    object MyPosts : AppScreen()
    object MyComments : AppScreen()
    object Favorites : AppScreen()
    object Notifications : AppScreen()
    object Privacy : AppScreen()
    object Emergency : AppScreen()
    object Help : AppScreen()
    object EditProfile : AppScreen()
    object Settings : AppScreen()
}

// Экраны для Bottom Navigation
sealed class Screen(
    val route: String,
    val title: String
) {
    object Feed : Screen("feed", "Лента")
    object Chat : Screen("chat", "Чат")
    object Profile : Screen("profile", "Профиль")
}

// Главный компонент приложения с навигацией
@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Main) }
    var currentBottomScreen by remember { mutableStateOf<Screen>(Screen.Feed) }

    when (currentScreen) {
        is AppScreen.Main -> {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        currentRoute = currentBottomScreen.route,
                        onItemSelected = { screen ->
                            currentBottomScreen = screen
                        }
                    )
                },
                floatingActionButton = {
                    // FAB кнопка показывается только на экране ленты
                    if (currentBottomScreen is Screen.Feed) {
                        FloatingActionButton(
                            onClick = {
                                currentScreen = AppScreen.CreatePost
                            },
                            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            contentColor = Color.Black
                        ) {
                            Text("+")
                        }
                    }
                }
            ) { paddingValues ->
                when (currentBottomScreen) {
                    is Screen.Feed -> FeedScreen(
                        onNavigate = { screen -> currentScreen = screen }
                    )
                    is Screen.Chat -> ChatScreen(
                        onChatClick = { chatName ->
                            currentScreen = AppScreen.ChatDetail(chatName)
                        }
                    )
                    is Screen.Profile -> ProfileScreen(
                        onNavigationClick = { screen -> currentScreen = screen }
                    )
                }
            }
        }

        is AppScreen.CreatePost -> {
            CreatePostScreen(
                onBackClick = { currentScreen = AppScreen.Main },
                onPostCreated = { currentScreen = AppScreen.Main }
            )
        }

        is AppScreen.ChatDetail -> {
            ChatDetailScreen(
                chatName = (currentScreen as AppScreen.ChatDetail).chatName,
                onBackClick = { currentScreen = AppScreen.Main }
            )
        }

        is AppScreen.PostDetails -> {
            PostDetailsScreen(
                postId = (currentScreen as AppScreen.PostDetails).postId,
                onBackClick = { currentScreen = AppScreen.Main },
                viewModel = remember {
                    PostDetailsViewModel(
                        postRepository = PostRepositoryImpl.getInstance(),
                        commentRepository = CommentRepositoryImpl()
                    )
                }
            )
        }

        // ПРОФИЛЬНЫЕ ЭКРАНЫ
        is AppScreen.MyPosts -> MyPostsScreen(onBackClick = { currentScreen = AppScreen.Main })
        is AppScreen.MyComments -> MyCommentsScreen(onBackClick = { currentScreen = AppScreen.Main })
        is AppScreen.Favorites -> FavoritesScreen(onBackClick = { currentScreen = AppScreen.Main })
        is AppScreen.Notifications -> NotificationsScreen(onBackClick = { currentScreen = AppScreen.Main })
        is AppScreen.Privacy -> PrivacyScreen(onBackClick = { currentScreen = AppScreen.Main })
        is AppScreen.Emergency -> EmergencyScreen(onBackClick = { currentScreen = AppScreen.Main })
        is AppScreen.Help -> HelpScreen(onBackClick = { currentScreen = AppScreen.Main })
        is AppScreen.EditProfile -> EditProfileScreen(onBackClick = { currentScreen = AppScreen.Main })
        is AppScreen.Settings -> SettingsScreen(onBackClick = { currentScreen = AppScreen.Main })
    }
}

@Preview
@Composable
fun MainAppPreview() {
    MainApp()
}