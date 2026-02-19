package com.example.newsfeedsimulator

import kotlinx.coroutines.flow.*

class NewsViewModel(private val repository: NewsRepository) {
    private val _readArticles = MutableStateFlow<Set<Int>>(emptySet())
    val readArticles: StateFlow<Set<Int>> = _readArticles.asStateFlow()

    val readCount: StateFlow<Int> = _readArticles
        .map { it.size }
        .stateIn(
            scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Default),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun fetchNews(category: String): Flow<NewsArticle> {
        return repository.getNewsStream(category)
    }

    fun markAsRead(articleId: Int) {
        _readArticles.value = _readArticles.value + articleId
    }
}