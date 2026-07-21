package com.zhengqi.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zhengqi.app.data.security.LockManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LockViewModel(application: Application) : AndroidViewModel(application) {
    val lockManager = LockManager(application)

    private val _isLockEnabled = MutableStateFlow(lockManager.isLockEnabled)
    val isLockEnabled: StateFlow<Boolean> = _isLockEnabled.asStateFlow()

    private val _lockType = MutableStateFlow(lockManager.lockType)
    val lockType: StateFlow<String> = _lockType.asStateFlow()

    private val _isLocked = MutableStateFlow(lockManager.isLockEnabled)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    fun setLockEnabled(enabled: Boolean) {
        lockManager.isLockEnabled = enabled
        _isLockEnabled.value = enabled
        _isLocked.value = enabled
    }

    fun setLockType(type: String) {
        lockManager.lockType = type
        _lockType.value = type
    }

    fun setDigitPassword(password: String) {
        lockManager.setDigitPassword(password)
    }

    fun setPatternPassword(pattern: String) {
        lockManager.setPatternPassword(pattern)
    }

    fun verifyDigitPassword(password: String): Boolean = lockManager.verifyDigitPassword(password)

    fun verifyPatternPassword(pattern: String): Boolean = lockManager.verifyPatternPassword(pattern)

    fun hasDigitPassword(): Boolean = lockManager.hasDigitPassword()

    fun hasPatternPassword(): Boolean = lockManager.hasPatternPassword()

    fun clearPatternPassword() {
        lockManager.clearPatternPassword()
    }

    fun unlock() {
        _isLocked.value = false
    }

    fun lock() {
        if (lockManager.isLockEnabled) {
            _isLocked.value = true
        }
    }

    fun disableLock() {
        lockManager.disableLock()
        _isLockEnabled.value = false
        _isLocked.value = false
    }
}