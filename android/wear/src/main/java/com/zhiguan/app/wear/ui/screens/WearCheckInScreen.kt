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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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

@Composable
fun WearCheckInScreen(onBack: () -> Unit) {
    val viewModel: WearViewModel = viewModel()
    val trackItems by viewModel.trackItems.collectAsState()
    val todayCheckIns by viewModel.todayCheckIns.collectAsState()

    val selectedIds = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(Unit) {
        viewModel.refreshStats()
    }

    LaunchedEffect(todayCheckIns) {
        if (selectedIds.isEmpty() && todayCheckIns.isNotEmpty()) {
            selectedIds.putAll(todayCheckIns)
        }
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
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Text(
                    text = "今日打卡",
                    style = MaterialTheme.typography.title3,
                    color = MaterialTheme.colors.primary
                )
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            if (trackItems.isEmpty()) {
                item {
                    Text(
                        text = "暂无打卡项目",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
            } else {
                items(trackItems.size) { index ->
                    val item = trackItems[index]
                    val isChecked = selectedIds[item.id] ?: false
                    Chip(
                        onClick = {
                            selectedIds[item.id] = !isChecked
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        icon = {
                            Icon(
                                imageVector = if (isChecked) Icons.Filled.CheckCircle
                                else Icons.Filled.RadioButtonUnchecked,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (isChecked) MaterialTheme.colors.primary
                                else MaterialTheme.colors.onSurfaceVariant
                            )
                        },
                        label = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = item.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        },
                        colors = if (isChecked) ChipDefaults.primaryChipColors()
                        else ChipDefaults.secondaryChipColors()
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Chip(
                    onClick = {
                        val checkedIds = selectedIds.filter { it.value }.keys.toList()
                        if (checkedIds.isNotEmpty()) {
                            viewModel.batchCheckIn(checkedIds, "", "")
                        }
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "确认打卡",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    colors = ChipDefaults.primaryChipColors()
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

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
                    },
                    colors = ChipDefaults.secondaryChipColors()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}