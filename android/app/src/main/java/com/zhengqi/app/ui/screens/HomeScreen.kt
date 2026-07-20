package com.zhengqi.app.ui.screens

import androidx.compose.animation.core.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhengqi.app.data.model.TrackItem
import com.zhengqi.app.ui.components.ProgressRing
import com.zhengqi.app.ui.components.QuoteCard
import com.zhengqi.app.ui.components.TrackItemIcons
import com.zhengqi.app.ui.components.ZhengQiButton
import com.zhengqi.app.ui.components.ZhengQiCard
import com.zhengqi.app.ui.theme.*
import com.zhengqi.app.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTrackConfig: () -> Unit,
    onNavigateToArticleDetail: (Long) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val trackItems by viewModel.trackItems.collectAsState()
    val todayCheckIns by viewModel.todayCheckIns.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val zhengQiScore by viewModel.zhengQiScore.collectAsState()
    val totalDays by viewModel.totalDays.collectAsState()
    val completionRate by viewModel.completionRate.collectAsState()
    val dailyQuote by viewModel.dailyQuote.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val animatedScore by animateIntAsState(
        targetValue = zhengQiScore,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 200f),
        label = "score"
    )

    LaunchedEffect(Unit) {
        viewModel.refreshStats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "正气",
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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (trackItems.isEmpty()) {
            // Empty state — Apple product-tile aesthetic
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.SelfImprovement,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "开始你的正气之旅",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "添加追踪项目，记录每日打卡",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ZhengQiButton(text = "添加追踪项", onClick = onNavigateToTrackConfig)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 17.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Streak and Score section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProgressRing(
                        progress = completionRate,
                        centerText = streak.toString(),
                        centerSubText = "连续打卡"
                    )

                    // ZhengQi Score Card — Action Blue tile
                    ZhengQiCard(
                        modifier = Modifier.width(160.dp),
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        showBorder = false,
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        Text(
                            text = "正气值",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = animatedScore.toString(),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontSize = 36.sp,
                                lineHeight = 40.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "总打卡 $totalDays 天",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Today's check-in list
                Text(
                    text = "今日打卡",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                trackItems.forEach { item ->
                    val isChecked = todayCheckIns[item.id] ?: false
                    CheckInItemCard(
                        trackItem = item,
                        isChecked = isChecked,
                        onClick = { viewModel.toggleCheckIn(item.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats summary
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "完成率",
                        value = "${(completionRate * 100).toInt()}%",
                        icon = Icons.Default.CheckCircle,
                        color = StatusGreen
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "总天数",
                        value = totalDays.toString(),
                        icon = Icons.Default.CalendarMonth,
                        color = MaterialTheme.colorScheme.primary
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "连续",
                        value = "${streak}天",
                        icon = Icons.Default.LocalFireDepartment,
                        color = StatusYellow
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Daily Quote
                if (dailyQuote != null) {
                    QuoteCard(quote = dailyQuote)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Recommended article link
                ZhengQiCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToArticleDetail(1) },
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    showBorder = false,
                    contentPadding = PaddingValues(17.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "推荐文章",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "点击查看最新修心文章",
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
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun CheckInItemCard(
    trackItem: TrackItem,
    isChecked: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "cardPress"
    )

    val icon = TrackItemIcons.fromName(trackItem.iconName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(MaterialTheme.shapes.medium)
            .background(if (isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface)
            .then(
                if (!isChecked) Modifier.border(1.dp, HairlineSoft, MaterialTheme.shapes.medium)
                else Modifier
            )
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
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
                .background(if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
