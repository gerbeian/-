package com.zhengqi.app

import android.app.Application
import com.zhengqi.app.data.local.AppDatabase
import com.zhengqi.app.data.security.LockManager

class ZhengQiApp : Application() {
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