package com.zhiguan.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhiguan.app.data.local.AppDatabase
import com.zhiguan.app.data.model.Article
import com.zhiguan.app.data.model.Quote
import com.zhiguan.app.data.repository.LearnRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LearnViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = LearnRepository(db.quoteDao(), db.articleDao())

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _favoriteArticles = MutableStateFlow<List<Article>>(emptyList())
    val favoriteArticles: StateFlow<List<Article>> = _favoriteArticles.asStateFlow()

    private val _randomQuote = MutableStateFlow<Quote?>(null)
    val randomQuote: StateFlow<Quote?> = _randomQuote.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    val tabs = listOf("名言", "修心", "励志", "养生")

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getRandomQuote().collect { quote ->
                _randomQuote.value = quote
            }
        }
        viewModelScope.launch {
            repository.getAllQuotes().collect { _quotes.value = it }
        }
        viewModelScope.launch {
            repository.getAllArticles().collect { _articles.value = it }
        }
        viewModelScope.launch {
            repository.getFavoriteArticles().collect { _favoriteArticles.value = it }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
        viewModelScope.launch {
            when (index) {
                0 -> repository.getAllQuotes().collect { _quotes.value = it }
                else -> {
                    val category = tabs[index]
                    repository.getArticlesByCategory(category).collect { _articles.value = it }
                }
            }
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
        viewModelScope.launch {
            repository.incrementReadCount(article.id)
        }
    }

    fun loadArticleById(id: Long) {
        viewModelScope.launch {
            val article = repository.getArticleById(id)
            if (article != null) {
                _selectedArticle.value = article
                repository.incrementReadCount(id)
            }
        }
    }

    fun toggleFavorite(article: Article) {
        viewModelScope.launch {
            repository.toggleFavorite(article.id, article.isFavorite)
            loadData()
        }
    }

    fun refreshRandomQuote() {
        viewModelScope.launch {
            repository.getRandomQuote().collect { quote ->
                _randomQuote.value = quote
            }
        }
    }
}