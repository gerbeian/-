package com.zhiguan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.zhiguan.app.ui.navigation.NavGraph
import com.zhiguan.app.ui.navigation.Routes
import com.zhiguan.app.ui.theme.ZhiGuanTheme
import com.zhiguan.app.viewmodel.LockViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZhiGuanTheme {
                val navController = rememberNavController()
                val lockViewModel: LockViewModel = viewModel()
                val isLocked by lockViewModel.isLocked.collectAsState()

                val startDestination = if (isLocked) Routes.LOCK else Routes.MAIN

                NavGraph(
                    navController = navController,
                    lockViewModel = lockViewModel,
                    startDestination = startDestination
                )
            }
        }
    }
}