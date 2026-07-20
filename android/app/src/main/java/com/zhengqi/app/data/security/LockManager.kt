package com.zhengqi.app.data.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class LockManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "zhengqi_lock_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_LOCK_ENABLED = "lock_enabled"
        private const val KEY_LOCK_TYPE = "lock_type" // "digit" or "pattern"
        private const val KEY_DIGIT_PASSWORD = "digit_password"
        private const val KEY_PATTERN_PASSWORD = "pattern_password"
    }

    var isLockEnabled: Boolean
        get() = prefs.getBoolean(KEY_LOCK_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_LOCK_ENABLED, value).apply()

    var lockType: String
        get() = prefs.getString(KEY_LOCK_TYPE, "digit") ?: "digit"
        set(value) = prefs.edit().putString(KEY_LOCK_TYPE, value).apply()

    fun setDigitPassword(password: String) {
        prefs.edit().putString(KEY_DIGIT_PASSWORD, password).apply()
    }

    fun setPatternPassword(pattern: String) {
        prefs.edit().putString(KEY_PATTERN_PASSWORD, pattern).apply()
    }

    fun verifyDigitPassword(password: String): Boolean {
        val stored = prefs.getString(KEY_DIGIT_PASSWORD, null) ?: return false
        return stored == password
    }

    fun verifyPatternPassword(pattern: String): Boolean {
        val stored = prefs.getString(KEY_PATTERN_PASSWORD, null) ?: return false
        return stored == pattern
    }

    fun hasDigitPassword(): Boolean = prefs.getString(KEY_DIGIT_PASSWORD, null) != null

    fun hasPatternPassword(): Boolean = prefs.getString(KEY_PATTERN_PASSWORD, null) != null

    fun disableLock() {
        isLockEnabled = false
        prefs.edit().remove(KEY_DIGIT_PASSWORD).remove(KEY_PATTERN_PASSWORD).apply()
    }
}