package com.zhiguan.app.wear.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import androidx.wear.compose.material.items
import com.zhiguan.app.wear.viewmodel.WearViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WearCalendarScreen(onBack: () -> Unit) {
    val viewModel: WearViewModel = viewModel()
    val checkInsByDate by viewModel.checkInsByDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedDateCheckIns by viewModel.selectedDateCheckIns.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshStats()
    }

    val today = LocalDate.now()
    val yearMonth = YearMonth.from(today)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfWeek = yearMonth.atDay(1).dayOfWeek.value // 1=Mon ... 7=Sun
    val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    val weekDays = listOf("一", "二", "三", "四", "五", "六", "日")

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
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.CHINESE)} ${yearMonth.year}",
                        style = MaterialTheme.typography.title3,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    weekDays.forEach { day ->
                        Text(
                            text = day,
                            style = MaterialTheme.typography.caption3,
                            color = MaterialTheme.colors.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(2.dp))
            }

            val totalCells = firstDayOfWeek - 1 + daysInMonth
            val rows = (totalCells + 6) / 7

            for (row in 0 until rows) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                    ) {
                        for (col in 0 until 7) {
                            val cellIndex = row * 7 + col
                            val dayNum = cellIndex - (firstDayOfWeek - 1) + 1

                            if (dayNum in 1..daysInMonth) {
                                val date = yearMonth.atDay(dayNum)
                                val dateStr = date.format(dateFormatter)
                                val hasCheckIns = checkInsByDate[dateStr]?.any { it.status } == true
                                val isSelected = dateStr == selectedDate
                                val isToday = date == today

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Chip(
                                        onClick = { viewModel.selectDate(dateStr) },
                                        modifier = Modifier.size(32.dp),
                                        colors = when {
                                            isSelected -> ChipDefaults.primaryChipColors()
                                            else -> ChipDefaults.secondaryChipColors()
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.Circle,
                                                contentDescription = null,
                                                modifier = Modifier.size(6.dp),
                                                tint = if (hasCheckIns) MaterialTheme.colors.primary
                                                else MaterialTheme.colors.surface
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = "$dayNum",
                                                style = MaterialTheme.typography.caption3,
                                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    )
                                    if (hasCheckIns) {
                                        Spacer(modifier = Modifier.height(1.dp))
                                        Icon(
                                            imageVector = Icons.Filled.FiberManualRecord,
                                            contentDescription = null,
                                            modifier = Modifier.size(6.dp),
                                            tint = MaterialTheme.colors.primary
                                        )
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = selectedDate,
                    style = MaterialTheme.typography.caption2,
                    color = MaterialTheme.colors.onSurfaceVariant
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (selectedDateCheckIns.isEmpty()) {
                item {
                    Text(
                        text = "当天无打卡记录",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
            } else {
                items(selectedDateCheckIns.size) { index ->
                    val checkIn = selectedDateCheckIns[index]
                    Chip(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        icon = {
                            Icon(
                                imageVector = if (checkIn.status) Icons.Filled.Circle else Icons.Filled.FiberManualRecord,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (checkIn.status) MaterialTheme.colors.primary
                                else MaterialTheme.colors.error
                            )
                        },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (checkIn.status) "✓" else "✗",
                                    color = if (checkIn.status) MaterialTheme.colors.primary
                                    else MaterialTheme.colors.error,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "项目 ${checkIn.trackItemId}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    )
                }
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