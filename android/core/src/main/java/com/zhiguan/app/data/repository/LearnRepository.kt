package com.zhiguan.app.data.repository

import com.zhiguan.app.data.local.ArticleDao
import com.zhiguan.app.data.local.QuoteDao
import com.zhiguan.app.data.model.Article
import com.zhiguan.app.data.model.Quote
import kotlinx.coroutines.flow.Flow

class LearnRepository(
    private val quoteDao: QuoteDao,
    private val articleDao: ArticleDao
) {
    fun getRandomQuote(): Flow<Quote?> = quoteDao.getRandomQuoteFlow()

    fun getQuotesByCategory(category: String): Flow<List<Quote>> = quoteDao.getQuotesByCategory(category)

    fun getAllQuotes(): Flow<List<Quote>> = quoteDao.getAllQuotes()

    fun getArticlesByCategory(category: String): Flow<List<Article>> = articleDao.getArticlesByCategory(category)

    fun getAllArticles(): Flow<List<Article>> = articleDao.getAllArticles()

    fun getFavoriteArticles(): Flow<List<Article>> = articleDao.getFavoriteArticles()

    suspend fun getArticleById(id: Long): Article? = articleDao.getById(id)

    suspend fun incrementReadCount(id: Long) = articleDao.incrementReadCount(id)

    suspend fun toggleFavorite(id: Long, currentFavorite: Boolean) {
        articleDao.setFavorite(id, !currentFavorite)
    }
}