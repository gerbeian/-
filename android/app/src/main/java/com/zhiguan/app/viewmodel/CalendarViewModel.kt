package com.zhiguan.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhiguan.app.data.local.AppDatabase
import com.zhiguan.app.data.model.CheckIn
import com.zhiguan.app.data.model.TrackItem
import com.zhiguan.app.data.repository.CheckInRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = CheckInRepository(db.checkInDao(), db.trackItemDao())

    private val _trackItems = MutableStateFlow<List<TrackItem>>(emptyList())
    val trackItems: StateFlow<List<TrackItem>> = _trackItems.asStateFlow()

    private val _checkInsByDate = MutableStateFlow<Map<String, List<CheckIn>>>(emptyMap())
    val checkInsByDate: StateFlow<Map<String, List<CheckIn>>> = _checkInsByDate.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedDateCheckIns = MutableStateFlow<List<CheckIn>>(emptyList())
    val selectedDateCheckIns: StateFlow<List<CheckIn>> = _selectedDateCheckIns.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            repository.getActiveTrackItems().collect { items ->
                _trackItems.value = items
            }
        }
        loadMonthData()
    }

    fun loadMonthData() {
        viewModelScope.launch {
            val currentMonth = YearMonth.now()
            val startDate = currentMonth.atDay(1).format(DateTimeFormatter.ISO_LOCAL_DATE)
            val endDate = currentMonth.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE)
            val checkIns = repository.getCheckInsBetween(startDate, endDate)
            _checkInsByDate.value = checkIns.groupBy { it.date }
        }
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
        viewModelScope.launch {
            val checkIns = repository.getCheckInsForDate(date)
            _selectedDateCheckIns.value = checkIns
        }
    }

    fun toggleCheckIn(trackItemId: Long, date: String) {
        viewModelScope.launch {
            repository.toggleCheckIn(trackItemId, date)
            selectDate(date)
            loadMonthData()
        }
    }
}