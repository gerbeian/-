package com.zhiguan.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_ins")
data class CheckIn(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trackItemId: Long,
    val date: String,
    val status: Boolean,
    val note: String = "",
    val imageUri: String = "",
    val createdAt: Long = System.currentTimeMillis()
)