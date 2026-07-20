package com.zhengqi.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Centralized mapping of track-item icon keys to Material vector icons.
 * Used by HomeScreen, CalendarScreen, and TrackItemConfigScreen so the
 * icon vocabulary stays consistent across the app.
 */
object TrackItemIcons {
    fun fromName(name: String): ImageVector = when (name) {
        "shield" -> Icons.Default.Shield
        "fitness" -> Icons.Default.FitnessCenter
        "bedtime" -> Icons.Default.Bedtime
        "book" -> Icons.Default.MenuBook
        "self_improvement" -> Icons.Default.SelfImprovement
        "star" -> Icons.Default.Star
        "favorite" -> Icons.Default.Favorite
        "water_drop" -> Icons.Default.WaterDrop
        "nature" -> Icons.Default.Nature
        "check" -> Icons.Default.CheckCircle
        else -> Icons.Default.CheckCircle
    }

    /** All selectable icon options for the TrackItemConfigScreen picker. */
    val options: List<Pair<String, String>> = listOf(
        "shield" to "盾牌",
        "fitness" to "运动",
        "bedtime" to "早睡",
        "book" to "阅读",
        "self_improvement" to "冥想",
        "check" to "打卡",
        "star" to "星标",
        "favorite" to "爱心",
        "water_drop" to "水滴",
        "nature" to "自然"
    )
}
