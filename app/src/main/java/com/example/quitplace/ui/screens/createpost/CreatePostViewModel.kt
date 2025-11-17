package com.example.quitplace.ui.screens.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitplace.data.repository.PostRepositoryImpl
import com.example.quitplace.domain.model.ProblemCategory
import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.model.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class CreatePostViewModel : ViewModel() {
    private val postRepository = PostRepositoryImpl()
    private val _uiState = MutableStateFlow(CreatePostState())
    val uiState: StateFlow<CreatePostState> = _uiState.asStateFlow()

    // ... весь ваш код остается таким же
    fun onTextChange(text: String) {
        _uiState.update {
            it.copy(
                text = text,
                charCount = text.length
            )
        }
    }

    fun selectCategory(category: ProblemCategory) {
        _uiState.update { it.copy(category = category) }
    }

    fun toggleTriggerWarning() {
        _uiState.update { it.copy(isTriggerWarning = !it.isTriggerWarning) }
    }

    fun applyTemplate(template: String) {
        _uiState.update {
            it.copy(
                text = template,
                charCount = template.length
            )
        }
    }

    fun submit() {
        if (!_uiState.value.isValid) return

        _uiState.update { it.copy(isSubmitting = true, error = null) }

        viewModelScope.launch {
            try {
                val newPost = Post(
                    id = UUID.randomUUID().toString(),
                    content = _uiState.value.text,
                    language = Language.detectFromText(_uiState.value.text),
                    category = _uiState.value.category!!,
                    triggerWarnings = emptySet(),
                    authorId = "current_user"
                )

                postRepository.createPost(newPost)

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        isPostCreated = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        error = "Ошибка публикации: ${e.message}"
                    )
                }
            }
        }
    }

    fun resetPostCreated() {
        _uiState.update { it.copy(isPostCreated = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetState() {
        _uiState.value = CreatePostState()
    }
}

data class CreatePostState(
    val text: String = "",
    val category: ProblemCategory? = null,
    val isTriggerWarning: Boolean = false,
    val charCount: Int = 0,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val isPostCreated: Boolean = false
) {
    val isValid: Boolean
        get() = text.length in 10..1000 && category != null
}