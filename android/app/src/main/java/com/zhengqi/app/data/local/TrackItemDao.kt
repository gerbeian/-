package com.zhengqi.app.data.local

import androidx.room.*
import com.zhengqi.app.data.model.TrackItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackItemDao {
    @Query("SELECT * FROM track_items WHERE isActive = 1 ORDER BY sortOrder ASC")
    fun getAllActive(): Flow<List<TrackItem>>

    @Query("SELECT * FROM track_items ORDER BY sortOrder ASC")
    fun getAll(): Flow<List<TrackItem>>

    @Query("SELECT * FROM track_items WHERE id = :id")
    suspend fun getById(id: Long): TrackItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TrackItem): Long

    @Update
    suspend fun update(item: TrackItem)

    @Delete
    suspend fun delete(item: TrackItem)

    @Query("UPDATE track_items SET isActive = 0 WHERE id = :id")
    suspend fun deactivate(id: Long)

    @Query("SELECT COUNT(*) FROM track_items WHERE isActive = 1")
    suspend fun getActiveCount(): Int
}