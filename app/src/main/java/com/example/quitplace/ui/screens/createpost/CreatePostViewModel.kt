package com.example.quitplace.ui.screens.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.model.ProblemCategory
import com.example.quitplace.domain.usecase.CreatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState = _uiState.asStateFlow()

    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }

    fun updateCategory(category: ProblemCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateTriggerWarning(show: Boolean, text: String = "") {
        _uiState.value = _uiState.value.copy(
            showTriggerWarning = show,
            triggerWarningText = if (show) text else ""
        )
    }

    fun createPost(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val state = _uiState.value

        if (state.content.length < 10) {
            onError("Пост должен содержать минимум 10 символов")
            return
        }

        viewModelScope.launch {
            try {
                val post = Post(
                    id = UUID.randomUUID().toString(),
                    content = state.content,
                    category = state.selectedCategory,
                    language = com.example.quitplace.domain.model.Language.RUSSIAN,
                    createdAt = Instant.now()
                )

                createPostUseCase(post)
                onSuccess()
            } catch (e: Exception) {
                onError("Ошибка при создании поста: ${e.message}")
            }
        }
    }
}

data class CreatePostUiState(
    val content: String = "",
    val selectedCategory: ProblemCategory = ProblemCategory.ANXIETY,
    val showTriggerWarning: Boolean = false,
    val triggerWarningText: String = ""
)