package com.zhengqi.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhengqi.app.ui.components.CheckInCalendar
import com.zhengqi.app.ui.components.TrackItemIcons
import com.zhengqi.app.ui.theme.CanvasWhite
import com.zhengqi.app.ui.theme.HairlineSoft
import com.zhengqi.app.ui.theme.StatusGreen
import com.zhengqi.app.viewmodel.CalendarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel()
) {
    val trackItems by viewModel.trackItems.collectAsState()
    val checkInsByDate by viewModel.checkInsByDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedDateCheckIns by viewModel.selectedDateCheckIns.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.selectDate(selectedDate)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "日历",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
                .padding(horizontal = 17.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            CheckInCalendar(
                checkInsByDate = checkInsByDate,
                totalTrackCount = trackItems.size,
                onDateSelected = { date -> viewModel.selectDate(date) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Selected date header
            Text(
                text = "${selectedDate} 打卡详情",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (trackItems.isEmpty()) {
                Text(
                    text = "暂无追踪项目，请先添加",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                trackItems.forEach { item ->
                    val checkIn = selectedDateCheckIns.find { it.trackItemId == item.id }
                    val isChecked = checkIn?.status == true
                    DateCheckInItem(
                        trackItem = item,
                        isChecked = isChecked,
                        onClick = { viewModel.toggleCheckIn(item.id, selectedDate) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DateCheckInItem(
    trackItem: com.zhengqi.app.data.model.TrackItem,
    isChecked: Boolean,
    onClick: () -> Unit
) {
    val icon = TrackItemIcons.fromName(trackItem.iconName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(if (isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface)
            .then(
                if (!isChecked) Modifier.border(1.dp, HairlineSoft, MaterialTheme.shapes.medium)
                else Modifier
            )
            .clickable { onClick() }
            .padding(horizontal = 17.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = trackItem.name,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        )
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(MaterialTheme.shapes.small)
                .background(if (isChecked) StatusGreen else MaterialTheme.colorScheme.outlineVariant),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = CanvasWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
