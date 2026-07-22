package com.zhiguan.app.data.local

import androidx.room.*
import com.zhiguan.app.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles WHERE category = :category ORDER BY createdAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<Article>>

    @Query("SELECT * FROM articles ORDER BY createdAt DESC")
    fun getAllArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun getById(id: Long): Article?

    @Query("UPDATE articles SET readCount = readCount + 1 WHERE id = :id")
    suspend fun incrementReadCount(id: Long)

    @Query("UPDATE articles SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun getCount(): Int
}