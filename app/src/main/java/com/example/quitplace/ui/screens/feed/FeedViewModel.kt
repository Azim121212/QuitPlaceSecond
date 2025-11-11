package com.example.quitplace.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.model.ProblemCategory
import com.example.quitplace.domain.usecase.GetPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            getPostsUseCase().collect { posts ->
                _uiState.value = _uiState.value.copy(
                    posts = posts,
                    isLoading = false
                )
            }
        }
    }

    fun selectCategory(category: ProblemCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}

data class FeedUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = true,
    val selectedCategory: ProblemCategory? = null,
    val searchQuery: String = ""
)