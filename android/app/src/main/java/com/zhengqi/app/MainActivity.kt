package com.zhengqi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.zhengqi.app.ui.navigation.NavGraph
import com.zhengqi.app.ui.navigation.Routes
import com.zhengqi.app.ui.theme.ZhengQiTheme
import com.zhengqi.app.viewmodel.LockViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZhengQiTheme {
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