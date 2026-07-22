package com.zhiguan.app.ui.components

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
        "music" -> Icons.Default.MusicNote
        "language" -> Icons.Default.Language
        "work" -> Icons.Default.Work
        "code" -> Icons.Default.Code
        "school" -> Icons.Default.School
        "medicine" -> Icons.Default.LocalHospital
        "gift" -> Icons.Default.CardGiftcard
        "pets" -> Icons.Default.Pets
        "palette" -> Icons.Default.Palette
        "camera" -> Icons.Default.Camera
        "flight" -> Icons.Default.Flight
        "restaurant" -> Icons.Default.Restaurant
        "directions_bike" -> Icons.Default.DirectionsBike
        "directions_walk" -> Icons.Default.DirectionsWalk
        "pool" -> Icons.Default.Pool
        "local_fire_department" -> Icons.Default.LocalFireDepartment
        "emoji_events" -> Icons.Default.EmojiEvents
        "psychology" -> Icons.Default.Psychology
        "water" -> Icons.Default.Water
        "eco" -> Icons.Default.Eco
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
        "nature" to "自然",
        "music" to "音乐",
        "language" to "语言",
        "work" to "工作",
        "code" to "编程",
        "school" to "学习",
        "medicine" to "健康",
        "gift" to "礼物",
        "pets" to "宠物",
        "palette" to "绘画",
        "camera" to "摄影",
        "flight" to "旅行",
        "restaurant" to "饮食",
        "directions_bike" to "骑行",
        "directions_walk" to "散步",
        "pool" to "游泳",
        "local_fire_department" to "火焰",
        "emoji_events" to "奖杯",
        "psychology" to "心理",
        "water" to "喝水",
        "eco" to "环保"
    )
}
