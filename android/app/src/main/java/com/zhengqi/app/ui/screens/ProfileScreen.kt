package com.zhengqi.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhengqi.app.R
import com.zhengqi.app.ui.components.ZhengQiButton
import com.zhengqi.app.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToStatistics: () -> Unit,
    onNavigateToTrackConfig: () -> Unit,
    onNavigateToLockSetup: () -> Unit,
    onNavigateToAbout: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val zhengQiScore by viewModel.zhengQiScore.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val totalDays by viewModel.totalDays.collectAsState()
    val exportResult by viewModel.exportResult.collectAsState()

    var showClearDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.refreshStats()
    }

    val level = when {
        zhengQiScore >= 1000 -> "正气大师"
        zhengQiScore >= 500 -> "正气高手"
        zhengQiScore >= 200 -> "正气学徒"
        zhengQiScore >= 50 -> "正气新手"
        else -> "正气入门"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "我的",
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
            Spacer(modifier = Modifier.height(12.dp))

            // Profile header — store-utility-card 规范（白底 + hairline 边框，无渐变）
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.large)
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 头像 — 圆形裁剪图片 + hairline 描边
                Image(
                    painter = painterResource(id = R.drawable.profile_avatar),
                    contentDescription = "头像",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = level,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "正气值 $zhengQiScore · 连续 $streak 天",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats row — 单一 Action Blue 强调色
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileStatItem("正气值", zhengQiScore.toString(), Icons.Default.Star)
                ProfileStatItem("连续天数", streak.toString(), Icons.Default.LocalFireDepartment)
                ProfileStatItem("总打卡", totalDays.toString(), Icons.Default.CheckCircle)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Menu items
            Text(
                text = "功能",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            ProfileMenuItem(
                icon = Icons.Default.BarChart,
                title = "统计图表",
                subtitle = "周频率 / 月视图 / 年视图",
                onClick = onNavigateToStatistics
            )
            ProfileMenuItem(
                icon = Icons.Default.Edit,
                title = "追踪项配置",
                subtitle = "自定义你的打卡项目",
                onClick = onNavigateToTrackConfig
            )
            ProfileMenuItem(
                icon = Icons.Default.Lock,
                title = "密码锁设置",
                subtitle = "手势密码 / 数字密码",
                onClick = onNavigateToLockSetup
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "数据",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            ProfileMenuItem(
                icon = Icons.Default.FileDownload,
                title = "导出数据",
                subtitle = "将打卡数据导出为 JSON 文件",
                onClick = { viewModel.exportData() }
            )
            ProfileMenuItem(
                icon = Icons.Default.DeleteForever,
                title = "清除数据",
                subtitle = "删除所有打卡记录",
                onClick = { showClearDialog = true },
                isDanger = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "其他",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            ProfileMenuItem(
                icon = Icons.Default.Info,
                title = "关于",
                subtitle = "版本 1.0.0",
                onClick = onNavigateToAbout
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // 导出结果提示
    exportResult?.let { result ->
        AlertDialog(
            onDismissRequest = { viewModel.clearExportResult() },
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = if (result.startsWith("导出成功")) "导出成功" else "导出失败",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = result,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.clearExportResult() }) {
                    Text("确定")
                }
            }
        )
    }

    // 清除数据确认弹窗
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = "确认清除",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(
                    text = "此操作将删除所有打卡记录，且不可恢复。确定要继续吗？",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAllData()
                        showClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("清除")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun ProfileStatItem(label: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDanger: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "menuPress"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isDanger) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = if (isDanger) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
