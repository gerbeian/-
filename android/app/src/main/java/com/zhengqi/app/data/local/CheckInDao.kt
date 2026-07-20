package com.zhengqi.app.data.local

import androidx.room.*
import com.zhengqi.app.data.model.CheckIn
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Query("SELECT * FROM check_ins WHERE date = :date ORDER BY createdAt DESC")
    suspend fun getCheckInsByDate(date: String): List<CheckIn>

    @Query("SELECT * FROM check_ins WHERE date = :date AND trackItemId = :trackItemId LIMIT 1")
    suspend fun getCheckIn(date: String, trackItemId: Long): CheckIn?

    @Query("SELECT * FROM check_ins WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getCheckInsBetween(startDate: String, endDate: String): List<CheckIn>

    @Query("SELECT * FROM check_ins WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getCheckInsBetweenFlow(startDate: String, endDate: String): Flow<List<CheckIn>>

    @Query("SELECT DISTINCT date FROM check_ins WHERE status = 1 ORDER BY date DESC")
    suspend fun getAllCheckInDates(): List<String>

    @Query("SELECT COUNT(DISTINCT date) FROM check_ins WHERE status = 1")
    suspend fun getTotalCheckInDays(): Int

    @Query("SELECT COUNT(*) FROM check_ins WHERE date = :date AND status = 1")
    suspend fun getCompletedCountForDate(date: String): Int

    @Query("SELECT COUNT(*) FROM check_ins WHERE date = :date")
    suspend fun getTotalCountForDate(date: String): Int

    @Query("SELECT * FROM check_ins WHERE date = :date AND status = 1 ORDER BY createdAt DESC")
    fun getCompletedCheckInsForDate(date: String): Flow<List<CheckIn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checkIn: CheckIn): Long

    @Update
    suspend fun update(checkIn: CheckIn)

    @Query("DELETE FROM check_ins WHERE date = :date AND trackItemId = :trackItemId")
    suspend fun deleteByDateAndTrack(date: String, trackItemId: Long)

    @Query("DELETE FROM check_ins")
    suspend fun deleteAll()
}