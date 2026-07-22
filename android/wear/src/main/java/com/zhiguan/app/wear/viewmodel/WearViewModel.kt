package com.zhiguan.app.wear.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhiguan.app.data.local.AppDatabase
import com.zhiguan.app.data.model.Article
import com.zhiguan.app.data.model.Quote
import com.zhiguan.app.data.repository.CheckInRepository
import com.zhiguan.app.data.repository.LearnRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WearViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = CheckInRepository(db.checkInDao(), db.trackItemDao())
    private val learnRepository = LearnRepository(db.quoteDao(), db.articleDao())

    private val _trackItems = MutableStateFlow<List<com.zhiguan.app.data.model.TrackItem>>(emptyList())
    val trackItems: StateFlow<List<com.zhiguan.app.data.model.TrackItem>> = _trackItems.asStateFlow()

    private val _todayCheckIns = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val todayCheckIns: StateFlow<Map<Long, Boolean>> = _todayCheckIns.asStateFlow()

    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak.asStateFlow()

    private val _completionRate = MutableStateFlow(0f)
    val completionRate: StateFlow<Float> = _completionRate.asStateFlow()

    private val _totalDays = MutableStateFlow(0)
    val totalDays: StateFlow<Int> = _totalDays.asStateFlow()

    private val _checkInsByDate = MutableStateFlow<Map<String, List<com.zhiguan.app.data.model.CheckIn>>>(emptyMap())
    val checkInsByDate: StateFlow<Map<String, List<com.zhiguan.app.data.model.CheckIn>>> = _checkInsByDate.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedDateCheckIns = MutableStateFlow<List<com.zhiguan.app.data.model.CheckIn>>(emptyList())
    val selectedDateCheckIns: StateFlow<List<com.zhiguan.app.data.model.CheckIn>> = _selectedDateCheckIns.asStateFlow()

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles.asStateFlow()

    private val _dailyQuote = MutableStateFlow<Quote?>(null)
    val dailyQuote: StateFlow<Quote?> = _dailyQuote.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getActiveTrackItems().collect { items -> _trackItems.value = items }
        }
        viewModelScope.launch {
            repository.getActiveTrackItems().first()
            refreshStats()
        }
        viewModelScope.launch {
            learnRepository.getAllArticles().collect { articles -> _articles.value = articles }
        }
        viewModelScope.launch {
            learnRepository.getRandomQuote().collect { quote -> _dailyQuote.value = quote }
        }
    }

    fun refreshStats() {
        viewModelScope.launch {
            _streak.value = repository.calculateStreak()
            _totalDays.value = repository.getTotalCheckInDays()
            _completionRate.value = repository.getTodayCompletionRate()

            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val checkIns = repository.getCheckInsForDate(today)
            val checkInMap = mutableMapOf<Long, Boolean>()
            _trackItems.value.forEach { item ->
                checkInMap[item.id] = checkIns.any { it.trackItemId == item.id && it.status }
            }
            _todayCheckIns.value = checkInMap

            val monthStart = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_LOCAL_DATE)
            val monthEnd = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)
            _checkInsByDate.value = repository.getCheckInsBetween(monthStart, monthEnd).groupBy { it.date }
        }
    }

    fun batchCheckIn(trackItemIds: List<Long>, note: String, imageUri: String) {
        viewModelScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            repository.batchCheckIn(trackItemIds, today, note, imageUri)
            refreshStats()
        }
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
        viewModelScope.launch {
            _selectedDateCheckIns.value = repository.getCheckInsForDate(date)
        }
    }

    fun toggleCheckIn(trackItemId: Long, date: String) {
        viewModelScope.launch {
            repository.toggleCheckIn(trackItemId, date)
            selectDate(date)
            refreshStats()
        }
    }
}