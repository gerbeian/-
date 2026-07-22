package com.zhiguan.app.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zhiguan.app.R
import com.zhiguan.app.data.security.AbstinenceManager
import com.zhiguan.app.ui.screens.*
import com.zhiguan.app.viewmodel.LockViewModel

object Routes {
    const val LOCK = "lock"
    const val MAIN = "main"
    const val HOME = "home"
    const val CALENDAR = "calendar"
    const val COMMUNITY = "community"
    const val LEARN = "learn"
    const val PROFILE = "profile"
    const val ARTICLE_DETAIL = "article/{articleId}"
    const val STATISTICS = "statistics"
    const val TRACK_CONFIG = "track_config"
    const val LOCK_SETUP = "lock_setup"
    const val EMERGENCY = "emergency"
    const val ABOUT = "about"

    fun articleDetail(id: Long) = "article/$id"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    lockViewModel: LockViewModel,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LOCK) {
            LockScreen(
                onUnlock = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOCK) { inclusive = true }
                    }
                },
                viewModel = lockViewModel
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                onNavigateToArticleDetail = { articleId ->
                    navController.navigate(Routes.articleDetail(articleId))
                },
                onNavigateToStatistics = {
                    navController.navigate(Routes.STATISTICS)
                },
                onNavigateToTrackConfig = {
                    navController.navigate(Routes.TRACK_CONFIG)
                },
                onNavigateToLockSetup = {
                    navController.navigate(Routes.LOCK_SETUP)
                },
                onNavigateToAbout = {
                    navController.navigate(Routes.ABOUT)
                },
                onNavigateToEmergency = {
                    navController.navigate(Routes.EMERGENCY)
                }
            )
        }

        composable(Routes.EMERGENCY) {
            val context = LocalContext.current
            EmergencyScreen(
                abstinenceManager = AbstinenceManager(context),
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            Routes.ARTICLE_DETAIL,
            arguments = listOf(navArgument("articleId") { type = NavType.LongType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getLong("articleId") ?: 0L
            ArticleDetailScreen(
                articleId = articleId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.STATISTICS) {
            StatisticsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.TRACK_CONFIG) {
            TrackItemConfigScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.LOCK_SETUP) {
            LockSetupScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "关于",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 17.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 顶部图片卡片 — store-utility-card 规范（白底 + hairline 边框 + 18dp 圆角），长方形保持原图比例
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.about_banner),
                    contentDescription = "应用图标",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 应用名称 — display-md / ink（非交互元素，不使用 primary）
            Text(
                "止观",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "版本 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 名言卡片 — store-utility-card 规范（parchment 底 + hairline 边框，无渐变）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "止观存内，邪不可干",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "帮助您养成自律习惯，提升身心健康",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 开发者署名 — fine-print
            Text(
                "Developed by vitBeian-GerBeian",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}