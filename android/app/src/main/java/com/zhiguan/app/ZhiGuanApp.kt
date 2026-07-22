package com.zhiguan.app

import android.app.Application
import com.zhiguan.app.data.local.AppDatabase
import com.zhiguan.app.data.security.LockManager

class ZhiGuanApp : Application() {
    lateinit var database: AppDatabase
        private set
    lateinit var lockManager: LockManager
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        lockManager = LockManager(this)
    }
}