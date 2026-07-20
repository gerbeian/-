package com.zhengqi.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val summary: String,
    val content: String,
    val category: String,
    val isFavorite: Boolean = false,
    val readCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)