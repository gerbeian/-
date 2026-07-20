package com.zhengqi.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhengqi.app.data.local.AppDatabase
import com.zhengqi.app.data.repository.CheckInRepository
import com.zhengqi.app.data.repository.LearnRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = CheckInRepository(db.checkInDao(), db.trackItemDao())
    private val learnRepository = LearnRepository(db.quoteDao(), db.articleDao())

    private val _trackItems = MutableStateFlow<List<com.zhengqi.app.data.model.TrackItem>>(emptyList())
    val trackItems: StateFlow<List<com.zhengqi.app.data.model.TrackItem>> = _trackItems.asStateFlow()

    private val _todayCheckIns = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val todayCheckIns: StateFlow<Map<Long, Boolean>> = _todayCheckIns.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    private val _zhengQiScore = MutableStateFlow(0)
    val zhengQiScore: StateFlow<Int> = _zhengQiScore.asStateFlow()

    private val _totalDays = MutableStateFlow(0)
    val totalDays: StateFlow<Int> = _totalDays.asStateFlow()

    private val _completionRate = MutableStateFlow(0f)
    val completionRate: StateFlow<Float> = _completionRate.asStateFlow()

    private val _dailyQuote = MutableStateFlow<com.zhengqi.app.data.model.Quote?>(null)
    val dailyQuote: StateFlow<com.zhengqi.app.data.model.Quote?> = _dailyQuote.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getActiveTrackItems().collect { items ->
                _trackItems.value = items
            }
        }
        viewModelScope.launch {
            learnRepository.getRandomQuote().collect { quote ->
                _dailyQuote.value = quote
            }
        }
        viewModelScope.launch {
            // Wait for track items to be loaded before refreshing stats
            repository.getActiveTrackItems().first()
            refreshStatsInternal()
        }
    }

    fun refreshStats() {
        viewModelScope.launch {
            refreshStatsInternal()
        }
    }

    private suspend fun refreshStatsInternal() {
        _streak.value = repository.calculateStreak()
        _zhengQiScore.value = repository.calculateZhengQiScore()
        _totalDays.value = repository.getTotalCheckInDays()
        _completionRate.value = repository.getTodayCompletionRate()
        _isLoading.value = false

        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val checkIns = repository.getCheckInsForDate(today)
        val checkInMap = mutableMapOf<Long, Boolean>()
        _trackItems.value.forEach { item ->
            checkInMap[item.id] = checkIns.any { it.trackItemId == item.id && it.status }
        }
        _todayCheckIns.value = checkInMap
    }

    fun toggleCheckIn(trackItemId: Long) {
        viewModelScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            repository.toggleCheckIn(trackItemId, today)
            refreshStats()
        }
    }
}