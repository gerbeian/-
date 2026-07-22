package com.zhiguan.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zhiguan.app.data.model.CheckIn
import com.zhiguan.app.ui.theme.StatusGray
import com.zhiguan.app.ui.theme.StatusGreen
import com.zhiguan.app.ui.theme.StatusYellow
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CheckInCalendar(
    checkInsByDate: Map<String, List<CheckIn>>,
    totalTrackCount: Int,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column(modifier = modifier) {
        // Month header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "上个月", tint = MaterialTheme.colorScheme.onBackground)
            }
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("yyyy年M月")),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "下个月", tint = MaterialTheme.colorScheme.onBackground)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Day of week headers
        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("一", "二", "三", "四", "五", "六", "日")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid
        val firstDay = currentMonth.atDay(1)
        val dayOfWeek = firstDay.dayOfWeek.value
        val daysInMonth = currentMonth.lengthOfMonth()
        val totalCells = (dayOfWeek - 1) + daysInMonth
        val rows = (totalCells + 6) / 7

        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        for (row in 0 until rows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..6) {
                    val cellIndex = row * 7 + col
                    val dayOfMonth = cellIndex - (dayOfWeek - 1) + 1

                    if (dayOfMonth in 1..daysInMonth) {
                        val date = currentMonth.atDay(dayOfMonth).format(DateTimeFormatter.ISO_LOCAL_DATE)
                        val checkIns = checkInsByDate[date] ?: emptyList()
                        val completed = checkIns.count { it.status }
                        val total = totalTrackCount

                        val cellColor = when {
                            total == 0 -> StatusGray
                            completed == total && total > 0 -> StatusGreen
                            completed > 0 -> StatusYellow
                            else -> Color.Transparent
                        }

                        val isToday = date == today

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(MaterialTheme.shapes.small)
                                .then(
                                    if (isToday) Modifier.border(
                                        1.5.dp,
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.shapes.small
                                    ) else Modifier
                                )
                                .background(if (cellColor != Color.Transparent) cellColor.copy(alpha = 0.3f) else Color.Transparent)
                                .clickable { onDateSelected(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = dayOfMonth.toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                )
                                if (total > 0 && completed > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(cellColor)
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
