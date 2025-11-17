package com.example.quitplace.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitplace.domain.model.ProblemCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

data class FeedState(
    val posts: List<PostUiModel> = emptyList(),
    val filteredPosts: List<PostUiModel> = emptyList(),
    val selectedCategory: ProblemCategory? = null,
    val isFilterPanelVisible: Boolean = false,
    val searchQuery: String = ""
)

data class PostUiModel(
    val id: String,
    val content: String,
    val category: ProblemCategory,
    val hasTriggerWarning: Boolean,
    val timestamp: String
)

class FeedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FeedState())
    val uiState: StateFlow<FeedState> = _uiState.asStateFlow()

    fun loadPosts() {
        viewModelScope.launch {
            // Генерируем mock данные
            val mockPosts = generateMockPosts()
            _uiState.update { it.copy(posts = mockPosts, filteredPosts = mockPosts) }
        }
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

    private fun generateMockPosts(): List<PostUiModel> {
        return listOf(
            PostUiModel(
                id = UUID.randomUUID().toString(),
                content = "Сегодня проснулась с чувством тяжелой тревоги. Кажется, будто что-то плохое должно случиться, хотя объективных причин нет. Как справляетесь с такими состояниями?",
                category = ProblemCategory.ANXIETY,
                hasTriggerWarning = false,
                timestamp = "10 минут назад"
            ),
            PostUiModel(
                id = UUID.randomUUID().toString(),
                content = "Прошла через тяжелый период депрессии. Хочу поделиться, что мне помогло: ежедневные прогулки, даже когда совсем не хочется, и разговор с психологом.",
                category = ProblemCategory.DEPRESSION,
                hasTriggerWarning = true,
                timestamp = "2 часа назад"
            ),
            PostUiModel(
                id = UUID.randomUUID().toString(),
                content = "Постоянные конфликты с партнером из-за мелочей. Чувствую, что мы отдаляемся друг от друга. Как вернуть понимание и доверие в отношениях?",
                category = ProblemCategory.RELATIONSHIPS,
                hasTriggerWarning = false,
                timestamp = "5 часов назад"
            ),
            PostUiModel(
                id = UUID.randomUUID().toString(),
                content = "Работа поглощает все мое время и силы. Просыпаюсь уставшим, нет энергии на хобби и общение. Похоже на выгорание?",
                category = ProblemCategory.WORK_STRESS,
                hasTriggerWarning = false,
                timestamp = "1 день назад"
            ),
            PostUiModel(
                id = UUID.randomUUID().toString(),
                content = "Бессонница замучила. Лежу часами, мысли крутятся в голове. Пробовала считать овец, медитацию - не помогает.",
                category = ProblemCategory.SLEEP,
                hasTriggerWarning = false,
                timestamp = "2 дня назад"
            ),
            PostUiModel(
                id = UUID.randomUUID().toString(),
                content = "Иногда кажется, что я недостаточно хорош. Сравниваю себя с другими и всегда проигрываю в этом сравнении.",
                category = ProblemCategory.GENERAL,
                hasTriggerWarning = false,
                timestamp = "3 дня назад"
            ),
            PostUiModel(
                id = UUID.randomUUID().toString(),
                content = "Панические атаки начались после аварии. Теперь боюсь садиться за руль и даже ехать в общественном транспорте.",
                category = ProblemCategory.FEAR,
                hasTriggerWarning = true,
                timestamp = "4 дня назад"
            ),
            PostUiModel(
                id = UUID.randomUUID().toString(),
                content = "Столкнулся с необычной проблемой - не могу выражать эмоции. Как будто внутри пустота, хотя понимаю, что должен что-то чувствовать.",
                category = ProblemCategory.OTHER,
                hasTriggerWarning = false,
                timestamp = "5 дней назад"
            )
        )
    }
}