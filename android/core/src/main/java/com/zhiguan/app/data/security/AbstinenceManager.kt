package com.zhiguan.app.data.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AbstinenceManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "zhiguan_abstinence_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_START_DATE = "abstinence_start_date"
        private const val KEY_LONGEST_STREAK = "longest_streak"
        private const val KEY_TOTAL_RELAPSES = "total_relapses"
        private const val KEY_RESISTED_COUNT = "resisted_count"
        private const val KEY_LAST_RESET_DATE = "last_reset_date"
        private const val KEY_GOAL_DAYS = "goal_days"
    }

    /** 获取戒色开始日期，如果从未设置则返回 null */
    val startDate: LocalDate?
        get() {
            val dateStr = prefs.getString(KEY_START_DATE, null) ?: return null
            return try {
                LocalDate.parse(dateStr)
            } catch (_: Exception) {
                null
            }
        }

    /** 获取当前戒色天数 */
    fun getCurrentStreak(): Int {
        val start = startDate ?: return 0
        return ChronoUnit.DAYS.between(start, LocalDate.now()).toInt()
    }

    /** 获取历史最长戒色天数 */
    val longestStreak: Int
        get() = prefs.getInt(KEY_LONGEST_STREAK, 0)

    /** 获取总破戒次数 */
    val totalRelapses: Int
        get() = prefs.getInt(KEY_TOTAL_RELAPSES, 0)

    /** 获取应急成功抵抗次数 */
    val resistedCount: Int
        get() = prefs.getInt(KEY_RESISTED_COUNT, 0)

    /** 获取戒色目标天数 */
    val goalDays: Int
        get() = prefs.getInt(KEY_GOAL_DAYS, 0)

    /** 设置戒色目标天数 */
    fun setGoalDays(days: Int) {
        prefs.edit().putInt(KEY_GOAL_DAYS, days).apply()
    }

    /** 获取目标进度 (0.0 ~ 1.0) */
    fun getGoalProgress(): Float {
        val goal = goalDays
        if (goal <= 0) return 0f
        return (getCurrentStreak().toFloat() / goal).coerceIn(0f, 1f)
    }

    /** 开始戒色（初次设置或重置后重新开始） */
    fun startAbstinence() {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        prefs.edit().putString(KEY_START_DATE, today).apply()
    }

    /** 破戒重置 */
    fun resetAbstinence() {
        val currentStreak = getCurrentStreak()
        val currentLongest = longestStreak
        if (currentStreak > currentLongest) {
            prefs.edit().putInt(KEY_LONGEST_STREAK, currentStreak).apply()
        }
        prefs.edit()
            .putInt(KEY_TOTAL_RELAPSES, totalRelapses + 1)
            .putString(KEY_LAST_RESET_DATE, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
            .apply()
        startAbstinence()
    }

    /** 记录一次成功抵抗诱惑 */
    fun recordResisted() {
        prefs.edit().putInt(KEY_RESISTED_COUNT, resistedCount + 1).apply()
    }

    /** 获取戒色阶段描述 */
    fun getStage(): AbstinenceStage {
        val days = getCurrentStreak()
        return when {
            days < 1 -> AbstinenceStage.STARTING
            days < 7 -> AbstinenceStage.FIRST_WEEK
            days < 14 -> AbstinenceStage.ONE_WEEK
            days < 21 -> AbstinenceStage.TWO_WEEKS
            days < 30 -> AbstinenceStage.THREE_WEEKS
            days < 60 -> AbstinenceStage.ONE_MONTH
            days < 90 -> AbstinenceStage.TWO_MONTHS
            days < 180 -> AbstinenceStage.THREE_MONTHS
            days < 365 -> AbstinenceStage.HALF_YEAR
            else -> AbstinenceStage.ONE_YEAR
        }
    }

    /** 获取下一个里程碑的天数目标和描述 */
    fun getNextMilestone(): Pair<Int, String> {
        val days = getCurrentStreak()
        val milestones = listOf(7, 14, 21, 30, 60, 90, 180, 365)
        for (milestone in milestones) {
            if (days < milestone) return milestone to formatMilestone(milestone)
        }
        return 0 to "已达成所有里程碑"
    }

    private fun formatMilestone(days: Int): String = when (days) {
        7 -> "一周"
        14 -> "两周"
        21 -> "三周"
        30 -> "一个月"
        60 -> "两个月"
        90 -> "三个月"
        180 -> "半年"
        365 -> "一年"
        else -> "${days}天"
    }
}

enum class AbstinenceStage(val label: String, val description: String) {
    STARTING("戒色起步", "千里之行，始于足下"),
    FIRST_WEEK("第一周", "最难熬的阶段，坚持就是胜利"),
    ONE_WEEK("一周达成", "身体开始适应，意志力在增强"),
    TWO_WEEKS("两周达成", "习惯开始形成，继续加油"),
    THREE_WEEKS("三周达成", "21天养成习惯，你已进入正轨"),
    ONE_MONTH("一个月", "生理和心理都发生了积极变化"),
    TWO_MONTHS("两个月", "你已战胜了绝大多数人"),
    THREE_MONTHS("三个月", "自律已成为你的名片"),
    HALF_YEAR("半年", "这是一个了不起的成就"),
    ONE_YEAR("一年", "你已经脱胎换骨，止观长存")
}