package com.zhengqi.app.data.repository

import com.zhengqi.app.data.local.CheckInDao
import com.zhengqi.app.data.local.TrackItemDao
import com.zhengqi.app.data.model.CheckIn
import com.zhengqi.app.data.model.TrackItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CheckInRepository(
    private val checkInDao: CheckInDao,
    private val trackItemDao: TrackItemDao
) {
    fun getActiveTrackItems(): Flow<List<TrackItem>> = trackItemDao.getAllActive()

    suspend fun getAllTrackItems(): List<TrackItem> = trackItemDao.getAllActive().first()

    suspend fun getCheckInsForDate(date: String): List<CheckIn> = checkInDao.getCheckInsByDate(date)

    suspend fun toggleCheckIn(trackItemId: Long, date: String): Boolean {
        val existing = checkInDao.getCheckIn(date, trackItemId)
        if (existing != null) {
            checkInDao.deleteByDateAndTrack(date, trackItemId)
            return false
        } else {
            checkInDao.insert(CheckIn(trackItemId = trackItemId, date = date, status = true))
            return true
        }
    }

    suspend fun checkIn(trackItemId: Long, date: String, note: String = "") {
        val existing = checkInDao.getCheckIn(date, trackItemId)
        if (existing == null) {
            checkInDao.insert(
                CheckIn(trackItemId = trackItemId, date = date, status = true, note = note)
            )
        }
    }

    suspend fun uncheckIn(trackItemId: Long, date: String) {
        checkInDao.deleteByDateAndTrack(date, trackItemId)
    }

    suspend fun getTotalCheckInDays(): Int = checkInDao.getTotalCheckInDays()

    suspend fun calculateStreak(): Int {
        val dates = checkInDao.getAllCheckInDates()
        if (dates.isEmpty()) return 0

        val sortedDates = dates.sortedDescending()
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)

        var streak = 0
        var currentDate = if (sortedDates.first() == today || sortedDates.first() == yesterday) {
            LocalDate.parse(sortedDates.first())
        } else {
            return 0
        }

        for (dateStr in sortedDates) {
            val date = LocalDate.parse(dateStr)
            if (date == currentDate) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else if (date == currentDate.minusDays(1)) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }

    suspend fun calculateZhengQiScore(): Int {
        val totalDays = checkInDao.getTotalCheckInDays()
        val streak = calculateStreak()
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val completedToday = checkInDao.getCompletedCountForDate(today)
        val totalToday = trackItemDao.getActiveCount()
        val completionRate = if (totalToday > 0) completedToday.toFloat() / totalToday else 0f

        val base = totalDays * 10
        val streakBonus = streak * streak * 2
        val completionBonus = (completionRate * 50).toInt()

        return base + streakBonus + completionBonus
    }

    suspend fun getTodayCompletionRate(): Float {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val completed = checkInDao.getCompletedCountForDate(today)
        val total = trackItemDao.getActiveCount()
        return if (total > 0) completed.toFloat() / total else 0f
    }

    suspend fun getCheckInsBetween(startDate: String, endDate: String): List<CheckIn> =
        checkInDao.getCheckInsBetween(startDate, endDate)

    suspend fun addTrackItem(name: String, iconName: String): Long {
        val count = trackItemDao.getActiveCount()
        return trackItemDao.insert(TrackItem(name = name, iconName = iconName, sortOrder = count))
    }

    suspend fun updateTrackItem(item: TrackItem) = trackItemDao.update(item)

    suspend fun deleteTrackItem(id: Long) = trackItemDao.deactivate(id)

    suspend fun deleteAllData() {
        checkInDao.deleteAll()
    }
}