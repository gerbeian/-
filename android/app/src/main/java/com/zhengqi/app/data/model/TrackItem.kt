package com.zhengqi.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_items")
data class TrackItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val iconName: String = "check",
    val isDefault: Boolean = false,
    val sortOrder: Int = 0,
    val isActive: Boolean = true
)