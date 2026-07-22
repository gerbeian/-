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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.delay

@Composable
fun WearEmergencyScreen(onBack: () -> Unit) {
    var breathingStep by remember { mutableStateOf(0) }
    val breathingSteps = listOf(
        "深吸气... 4秒" to 4000L,
        "屏住呼吸... 4秒" to 4000L,
        "缓慢呼气... 6秒" to 6000L
    )

    LaunchedEffect(Unit) {
        while (true) {
            val (_, duration) = breathingSteps[breathingStep]
            delay(duration)
            breathingStep = (breathingStep + 1) % breathingSteps.size
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
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colors.error
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "紧急模式",
                        style = MaterialTheme.typography.title3,
                        color = MaterialTheme.colors.error
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = breathingSteps[breathingStep].first,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = "应急方法",
                    style = MaterialTheme.typography.caption2,
                    color = MaterialTheme.colors.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Chip(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ChipDefaults.secondaryChipColors(),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.LocalDrink,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "喝一杯冷水",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Chip(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ChipDefaults.secondaryChipColors(),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.DirectionsRun,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "立即运动",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Chip(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ChipDefaults.secondaryChipColors(),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.WaterDrop,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "冷水洗脸",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Chip(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ChipDefaults.secondaryChipColors(),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "给朋友打电话",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Chip(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ChipDefaults.secondaryChipColors(),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.AutoStories,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "背诵戒色口诀",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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