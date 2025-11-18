package com.example.quitplace.ui.screens.postdetails

import androidx.compose.runtime.State
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quitplace.domain.model.Comment
import com.example.quitplace.domain.model.Post
import com.example.quitplace.domain.model.ProblemCategory
import com.example.quitplace.domain.repository.CommentRepository
import com.example.quitplace.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID


data class PostDetailsState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val newCommentText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSendingComment: Boolean = false
)

class PostDetailsViewModel(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostDetailsState())
    val uiState: StateFlow<PostDetailsState> = _uiState.asStateFlow()

    fun loadPost(postId: String) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val post = postRepository.getPostById(postId)
                _uiState.update { it.copy(post = post, isLoading = false) }
                loadComments(postId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Ошибка загрузки поста", isLoading = false) }
            }
        }
    }

    fun loadComments(postId: String) {
        viewModelScope.launch {
            try {
                val comments = commentRepository.getCommentsByPostId(postId)
                _uiState.update { it.copy(comments = comments) }
            } catch (e: Exception) {
                // Ignore comment loading errors for now
            }
        }
    }

    fun onCommentTextChanged(text: String) {
        _uiState.update { it.copy(newCommentText = text) }
    }

    fun sendComment() {
        val post = _uiState.value.post ?: return
        val text = _uiState.value.newCommentText.trim()

        if (text.length !in 2..500) return

        _uiState.update { it.copy(isSendingComment = true) }
        viewModelScope.launch {
            try {
                val comment = Comment(
                    id = UUID.randomUUID().toString(),
                    postId = post.id,
                    username = "Аноним #${(1000..9999).random()}",
                    text = text,
                    timestamp = System.currentTimeMillis()
                )
                commentRepository.addComment(comment)
                _uiState.update {
                    it.copy(
                        newCommentText = "",
                        isSendingComment = false,
                        comments = it.comments + comment
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Ошибка отправки комментария", isSendingComment = false) }
            }
        }
    }

    fun retry() {
        _uiState.value.post?.let { loadPost(it.id) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailsScreen(
    postId: String,
    onBackClick: () -> Unit,
    viewModel: PostDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var showCommentSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(postId) {
        viewModel.loadPost(postId)
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Просмотр поста",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(Icons.Filled.Share, "Поделиться")
                    }
                    IconButton(onClick = { /* TODO: Bookmark */ }) {
                        Icon(Icons.Filled.Bookmark, "Сохранить")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = listState.canScrollForward,
                enter = fadeIn() + slideInVertically()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(uiState.comments.size)
                        }
                    },
                    icon = { Icon(Icons.Filled.ExpandLess, "Вниз") },
                    text = { Text("К комментариям") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Ошибка загрузки",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedButton(onClick = { viewModel.retry() }) {
                            Text("Попробовать снова")
                        }
                    }
                }
                uiState.post != null -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)  // 16dp standard
                    ) {
                        item {
                            PostContent(
                                post = uiState.post!!,
                                onCommentClick = { showCommentSheet = true }
                            )
                        }

                        item {
                            CommentsHeader(
                                commentCount = uiState.comments.size,
                                modifier = Modifier.padding(vertical = 24.dp)  // 24dp sections
                            )
                        }

                        if (uiState.comments.isEmpty()) {
                            item {
                                EmptyCommentsState(
                                    modifier = Modifier.padding(vertical = 32.dp)
                                )
                            }
                        } else {
                            items(
                                items = uiState.comments,
                                key = { it.id }
                            ) { comment ->
                                CommentItem(
                                    comment = comment,
                                    modifier = Modifier.padding(vertical = 8.dp)  // 8dp minor spacing
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCommentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCommentSheet = false },
            sheetState = bottomSheetState
        ) {
            AddCommentBottomSheet(
                commentText = uiState.newCommentText,
                isSending = uiState.isSendingComment,
                onTextChange = { viewModel.onCommentTextChanged(it) },
                onSendClick = {
                    viewModel.sendComment()
                    showCommentSheet = false
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun PostContent(
    post: Post,
    onCommentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),  // Мягкая тень 2dp
        shape = RoundedCornerShape(20.dp),  // 20dp скругление (16-24dp range)
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)  // 24dp sections spacing
        ) {
            // Category Badge - лавандовый акцент
            Badge(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = post.category.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Normal
                )
            }

            Spacer(Modifier.height(16.dp))  // 16dp standard spacing

            // Trigger warning
            if (post.isTriggerWarning) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = "Триггер-предупреждение",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            "Триггер-предупреждение: чувствительный контент",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Post Text - полный текст с теплым off-white
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyLarge,  // bodyLarge для основного текста (24sp line height уже в типографике)
                color = MaterialTheme.colorScheme.onSurface  // Теплый off-white
            )

            Spacer(Modifier.height(16.dp))  // 16dp standard

            // Timestamp - вторичный текст
            Text(
                text = formatTimestamp(post.createdAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant  // WarmOffWhiteVariant
            )

            Spacer(Modifier.height(24.dp))  // 24dp sections

            // Comment button
            Button(
                onClick = onCommentClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Оставить комментарий")
            }
        }
    }
}

@Composable
fun CommentsHeader(
    commentCount: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Комментарии ($commentCount)",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
fun EmptyCommentsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Пока нет комментариев",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Будьте первым, кто поддержит автора",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),  // Мягкая тень
        shape = RoundedCornerShape(16.dp),  // 16dp скругление
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)  // 16dp standard
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.size(12.dp))

            // Comment content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = comment.username,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = formatTimestamp(comment.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.size(4.dp))

                Text(
                    text = comment.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommentBottomSheet(
    commentText: String,
    isSending: Boolean,
    onTextChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val charCount = commentText.length
    val isError = charCount !in 2..500
    val isValid = charCount in 2..500

    Column(modifier = modifier) {
        Text(
            text = "Новый комментарий",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        TextField(
            value = commentText,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Напишите комментарий поддержки...") },
            isError = isError,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = false,
            maxLines = 5
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$charCount / 500",
                style = MaterialTheme.typography.labelSmall,
                color = if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = onSendClick,
                enabled = isValid && !isSending
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Filled.Send, "Отправить")
                }
                Spacer(Modifier.size(8.dp))
                Text("Отправить")
            }
        }
    }
}

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

// Extension for collectAsState
@Preview
@Composable
fun PostDetailsScreenPreview() {
    MaterialTheme {
        PostDetailsScreen(
            postId = "1",
            onBackClick = {},
            viewModel = PostDetailsViewModel(
                postRepository = object : PostRepository {
                    override suspend fun getPostById(id: String): Post {
                        return Post(
                            id = "1",
                            content = "Это тестовый пост с содержанием, которое требует поддержки и понимания.",
                            category = ProblemCategory.ANXIETY,
                            triggerWarnings = setOf(),
                            createdAt = System.currentTimeMillis() - 2 * 60 * 60 * 1000
                        )
                    }
                    override suspend fun createPost(post: Post) {}
                    override fun getPosts(): kotlinx.coroutines.flow.Flow<List<Post>> = kotlinx.coroutines.flow.flowOf(emptyList())
                },
                commentRepository = object : CommentRepository {
                    override suspend fun getCommentsByPostId(postId: String): List<Comment> {
                        return listOf(
                            Comment(
                                id = "1",
                                postId = "1",
                                username = "Аноним #1234",
                                text = "Я понимаю ваши чувства. Вы не одни.",
                                timestamp = System.currentTimeMillis() - 30 * 60 * 1000
                            )
                        )
                    }
                    override suspend fun addComment(comment: Comment) {}
                }
            )
        )
    }
}