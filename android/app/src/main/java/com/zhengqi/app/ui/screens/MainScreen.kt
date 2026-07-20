package com.zhengqi.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun MainScreen(
    onNavigateToArticleDetail: (Long) -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToTrackConfig: () -> Unit,
    onNavigateToLockSetup: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    val navItems = listOf(
        BottomNavItem("首页", Icons.Default.Home, "home"),
        BottomNavItem("日历", Icons.Default.CalendarMonth, "calendar"),
        BottomNavItem("社区", Icons.Default.Groups, "community"),
        BottomNavItem("学习", Icons.Default.MenuBook, "learn"),
        BottomNavItem("我的", Icons.Default.Person, "profile")
    )

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = if (selectedTab == index) MaterialTheme.typography.labelSmall
                                else MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> HomeScreen(
                    onNavigateToTrackConfig = onNavigateToTrackConfig,
                    onNavigateToArticleDetail = onNavigateToArticleDetail
                )
                1 -> CalendarScreen()
                2 -> CommunityScreen()
                3 -> LearnScreen(
                    onArticleClick = onNavigateToArticleDetail
                )
                4 -> ProfileScreen(
                    onNavigateToStatistics = onNavigateToStatistics,
                    onNavigateToTrackConfig = onNavigateToTrackConfig,
                    onNavigateToLockSetup = onNavigateToLockSetup,
                    onNavigateToAbout = onNavigateToAbout
                )
            }
        }
    }
}
