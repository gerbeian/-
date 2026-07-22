package com.zhiguan.app.wear.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.zhiguan.app.data.security.AbstinenceManager
import com.zhiguan.app.wear.viewmodel.WearViewModel

@Composable
fun WearStatsScreen(onBack: () -> Unit) {
    val viewModel: WearViewModel = viewModel()
    val streak by viewModel.streak.collectAsState()
    val totalDays by viewModel.totalDays.collectAsState()
    val completionRate by viewModel.completionRate.collectAsState()

    val context = LocalContext.current
    val abstinenceManager = remember { AbstinenceManager(context) }

    LaunchedEffect(Unit) {
        viewModel.refreshStats()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        timeText = { TimeText() }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.BarChart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "统计",
                        style = MaterialTheme.typography.title3,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                StatChip(
                    icon = Icons.Filled.Timer,
                    label = "戒色天数",
                    value = "${abstinenceManager.getCurrentStreak()}",
                    unit = "天"
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                StatChip(
                    icon = Icons.Filled.EmojiEvents,
                    label = "最长戒色",
                    value = "${abstinenceManager.longestStreak}",
                    unit = "天"
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                StatChip(
                    icon = Icons.Filled.Repeat,
                    label = "连续打卡",
                    value = "$streak",
                    unit = "天"
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                StatChip(
                    icon = Icons.Filled.CalendarToday,
                    label = "累计打卡",
                    value = "$totalDays",
                    unit = "天"
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                StatChip(
                    icon = Icons.Filled.PieChart,
                    label = "今日完成率",
                    value = "${(completionRate * 100).toInt()}",
                    unit = "%"
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                StatChip(
                    icon = Icons.Filled.Warning,
                    label = "破戒次数",
                    value = "${abstinenceManager.totalRelapses}",
                    unit = "次"
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                StatChip(
                    icon = Icons.Filled.CheckCircle,
                    label = "成功抵抗",
                    value = "${abstinenceManager.resistedCount}",
                    unit = "次"
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Chip(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "返回",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun StatChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    unit: String
) {
    Chip(
        onClick = { },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        colors = ChipDefaults.secondaryChipColors(),
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colors.onSurfaceVariant
            )
        },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.title2,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.caption2,
                        color = MaterialTheme.colors.onSurfaceVariant
                    )
                }
            }
        }
    )
}