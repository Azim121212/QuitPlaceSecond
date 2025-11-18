package com.example.quitplace.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitplace.data.repository.PostRepositoryImpl
import com.example.quitplace.domain.model.ProblemCategory
import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeedState(
    val posts: List<PostUiModel> = emptyList(),
    val filteredPosts: List<PostUiModel> = emptyList(),
    val selectedCategory: ProblemCategory? = null,
    val isFilterPanelVisible: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

data class PostUiModel(
    val id: String,
    val content: String,
    val category: ProblemCategory,
    val hasTriggerWarning: Boolean,
    val timestamp: String
)

class FeedViewModel : ViewModel() {
    private val postRepository: PostRepository = PostRepositoryImpl
    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState.asStateFlow()

    init {
        // Подписываемся на посты из репозитория
        viewModelScope.launch {
            postRepository.getPosts()
                .map { posts -> posts.map { it.toPostUiModel() } }
                .collect { postUiModels ->
                    _uiState.update { state ->
                        val filtered = if (state.selectedCategory == null) {
                            postUiModels
                        } else {
                            postUiModels.filter { it.category == state.selectedCategory }
                        }
                        state.copy(
                            posts = postUiModels,
                            filteredPosts = filtered,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun loadPosts() {
        // Посты загружаются автоматически через Flow, но можно обновить состояние загрузки
        _uiState.update { it.copy(isLoading = true, error = null) }
    }

    fun toggleFilterPanel() {
        _uiState.update { it.copy(isFilterPanelVisible = !it.isFilterPanelVisible) }
    }

    fun selectCategory(category: ProblemCategory?) {
        _uiState.update { state ->
            val filtered = if (category == null) {
                state.posts
            } else {
                state.posts.filter { it.category == category }
            }
            state.copy(selectedCategory = category, filteredPosts = filtered)
        }
    }

    // Преобразование Post в PostUiModel
    private fun Post.toPostUiModel(): PostUiModel {
        return PostUiModel(
            id = this.id,
            content = this.content,
            category = this.category,
            hasTriggerWarning = this.isTriggerWarning,
            timestamp = formatTimestamp(this.createdAt)
        )
    }

    // Форматирование времени
    private fun formatTimestamp(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        val minutes = diff / (1000 * 60)
        val hours = diff / (1000 * 60 * 60)
        val days = diff / (1000 * 60 * 60 * 24)

        return when {
            minutes < 1 -> "Только что"
            minutes < 60 -> "$minutes мин назад"
            hours < 24 -> "$hours ч назад"
            days == 1L -> "Вчера"
            days < 7 -> "$days дн назад"
            else -> "${days / 7} нед назад"
        }
    }
}