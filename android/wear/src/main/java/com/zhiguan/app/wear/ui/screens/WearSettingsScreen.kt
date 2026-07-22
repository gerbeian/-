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
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.zhiguan.app.data.security.AbstinenceManager
import com.zhiguan.app.data.security.LockManager

@Composable
fun WearSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val abstinenceManager = remember { AbstinenceManager(context) }
    val lockManager = remember { LockManager(context) }

    var showResetConfirm by remember { mutableStateOf(false) }

    val lockStatus = if (lockManager.isLockEnabled) "已启用" else "未启用"
    val goalDays = abstinenceManager.goalDays
    val goalText = if (goalDays > 0) "${goalDays}天" else "未设置"

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "设置",
                        style = MaterialTheme.typography.title3,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Chip(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ChipDefaults.secondaryChipColors(),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
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
                                text = "手势密码",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = lockStatus,
                                style = MaterialTheme.typography.caption2,
                                color = MaterialTheme.colors.onSurfaceVariant
                            )
                        }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                Chip(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = ChipDefaults.secondaryChipColors(),
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Flag,
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
                                text = "戒色目标",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface
                            )
                            Text(
                                text = goalText,
                                style = MaterialTheme.typography.caption2,
                                color = MaterialTheme.colors.onSurfaceVariant
                            )
                        }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(4.dp)) }

            if (!showResetConfirm) {
                item {
                    Chip(
                        onClick = { showResetConfirm = true },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        colors = ChipDefaults.secondaryChipColors(),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.DeleteForever,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colors.error
                            )
                        },
                        label = {
                            Text(
                                text = "重置数据",
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.error,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
            } else {
                item {
                    Text(
                        text = "确定要重置所有数据？",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                }
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item {
                    Chip(
                        onClick = {
                            abstinenceManager.resetAbstinence()
                            showResetConfirm = false
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        colors = ChipDefaults.primaryChipColors(),
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.DeleteForever,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "确认重置",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item {
                    Chip(
                        onClick = { showResetConfirm = false },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                        colors = ChipDefaults.secondaryChipColors(),
                        label = {
                            Text(
                                text = "取消",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    )
                }
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
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}