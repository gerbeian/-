package com.zhengqi.app.viewmodel

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhengqi.app.data.local.AppDatabase
import com.zhengqi.app.data.model.TrackItem
import com.zhengqi.app.data.repository.CheckInRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = CheckInRepository(db.checkInDao(), db.trackItemDao())

    private val _trackItems = MutableStateFlow<List<TrackItem>>(emptyList())
    val trackItems: StateFlow<List<TrackItem>> = _trackItems.asStateFlow()

    private val _zhengQiScore = MutableStateFlow(0)
    val zhengQiScore: StateFlow<Int> = _zhengQiScore.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    private val _totalDays = MutableStateFlow(0)
    val totalDays: StateFlow<Int> = _totalDays.asStateFlow()

    private val _weeklyData = MutableStateFlow<Map<String, Int>>(emptyMap())
    val weeklyData: StateFlow<Map<String, Int>> = _weeklyData.asStateFlow()

    private val _monthlyData = MutableStateFlow<Map<String, Float>>(emptyMap())
    val monthlyData: StateFlow<Map<String, Float>> = _monthlyData.asStateFlow()

    private val _yearlyData = MutableStateFlow<Map<String, Int>>(emptyMap())
    val yearlyData: StateFlow<Map<String, Int>> = _yearlyData.asStateFlow()

    private val _exportResult = MutableStateFlow<String?>(null)
    val exportResult: StateFlow<String?> = _exportResult.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getActiveTrackItems().collect { items ->
                _trackItems.value = items
            }
        }
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch {
            _zhengQiScore.value = repository.calculateZhengQiScore()
            _streak.value = repository.calculateStreak()
            _totalDays.value = repository.getTotalCheckInDays()
        }
        loadChartData()
    }

    private fun loadChartData() {
        viewModelScope.launch {
            // Weekly data
            val today = LocalDate.now()
            val weekData = mutableMapOf<String, Int>()
            val daysOfWeek = listOf("一", "二", "三", "四", "五", "六", "日")
            for (i in 6 downTo 0) {
                val date = today.minusDays(i.toLong())
                val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                val checkIns = repository.getCheckInsForDate(dateStr)
                weekData[daysOfWeek[6 - i]] = checkIns.count { it.status }
            }
            _weeklyData.value = weekData

            // Monthly data
            val monthData = mutableMapOf<String, Float>()
            val firstDay = today.withDayOfMonth(1)
            val lastDay = today.withDayOfMonth(today.lengthOfMonth())
            val checkIns = repository.getCheckInsBetween(
                firstDay.format(DateTimeFormatter.ISO_LOCAL_DATE),
                lastDay.format(DateTimeFormatter.ISO_LOCAL_DATE)
            )
            val checkInsByDate = checkIns.groupBy { it.date }
            val totalItems = repository.getAllTrackItems().size
            for (i in 1..today.dayOfMonth) {
                val date = today.withDayOfMonth(i).format(DateTimeFormatter.ISO_LOCAL_DATE)
                val dayCheckIns = checkInsByDate[date] ?: emptyList()
                val completed = dayCheckIns.count { it.status }
                monthData[i.toString()] = if (totalItems > 0) completed.toFloat() / totalItems else 0f
            }
            _monthlyData.value = monthData

            // Yearly data
            val yearData = mutableMapOf<String, Int>()
            for (dateStr in checkIns.map { it.date }.distinct()) {
                yearData[dateStr] = checkIns.count { it.date == dateStr && it.status }
            }
            _yearlyData.value = yearData
        }
    }

    fun addTrackItem(name: String, iconName: String) {
        viewModelScope.launch {
            repository.addTrackItem(name, iconName)
            loadData()
        }
    }

    fun updateTrackItem(item: TrackItem) {
        viewModelScope.launch {
            repository.updateTrackItem(item)
            loadData()
        }
    }

    fun deleteTrackItem(id: Long) {
        viewModelScope.launch {
            repository.deleteTrackItem(id)
            loadData()
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            repository.deleteAllData()
            loadData()
        }
    }

    fun exportData() {
        viewModelScope.launch {
            try {
                val checkIns = repository.getAllCheckIns()
                val trackItems = repository.getAllTrackItems()
                val trackItemMap = trackItems.associateBy { it.id }

                val jsonArray = JSONArray()
                for (checkIn in checkIns) {
                    val obj = JSONObject()
                    obj.put("date", checkIn.date)
                    obj.put("trackItem", trackItemMap[checkIn.trackItemId]?.name ?: "未知")
                    obj.put("status", checkIn.status)
                    obj.put("note", checkIn.note)
                    if (checkIn.imageUri.isNotEmpty()) {
                        obj.put("imageUri", checkIn.imageUri)
                    }
                    jsonArray.put(obj)
                }

                val downloadsDir = getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    ?: getApplication<Application>().filesDir
                val file = File(downloadsDir, "正气_打卡数据_${LocalDate.now()}.json")
                file.writeText(jsonArray.toString(2))

                _exportResult.value = "导出成功：${file.absolutePath}"
            } catch (e: Exception) {
                _exportResult.value = "导出失败：${e.message}"
            }
        }
    }

    fun clearExportResult() {
        _exportResult.value = null
    }
}