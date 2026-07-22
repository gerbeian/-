package com.zhiguan.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhiguan.app.data.security.AbstinenceManager
import com.zhiguan.app.ui.theme.StatusGreen
import com.zhiguan.app.ui.theme.SystemRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(
    abstinenceManager: AbstinenceManager,
    onBack: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    var resisted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "应急模式",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        if (resisted) {
            // 成功抵抗后的庆祝页面
            SuccessResistedScreen(
                modifier = Modifier.padding(paddingValues),
                abstinenceManager = abstinenceManager,
                onBack = onBack
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 17.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // 戒色天数展示
                AbstinenceCounterCard(abstinenceManager = abstinenceManager)

                Spacer(modifier = Modifier.height(24.dp))

                when (currentStep) {
                    0 -> BreathingGuide(onComplete = { currentStep = 1 })
                    1 -> DistractionList(onNext = { currentStep = 2 })
                    2 -> MotivationalQuotes(onNext = { currentStep = 0 })
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 底部按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("返回")
                    }
                    Button(
                        onClick = {
                            abstinenceManager.recordResisted()
                            resisted = true
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatusGreen
                        )
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("我成功抵抗了")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun AbstinenceCounterCard(abstinenceManager: AbstinenceManager) {
    val days = abstinenceManager.getCurrentStreak()
    val stage = abstinenceManager.getStage()
    val (nextMilestone, nextLabel) = abstinenceManager.getNextMilestone()
    val progress = if (nextMilestone > 0) days.toFloat() / nextMilestone else 1f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "已戒色",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$days",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 56.sp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "天",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stage.label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
        )
        Text(
            text = stage.description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
        )

        if (nextMilestone > 0) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "距离「${nextLabel}」还有 ${nextMilestone - days} 天",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun BreathingGuide(onComplete: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathAlpha"
    )

    var breathPhase by remember { mutableStateOf(0) } // 0: 吸气, 1: 屏息, 2: 呼气
    var seconds by remember { mutableIntStateOf(4) }

    LaunchedEffect(Unit) {
        while (true) {
            // 吸气 4秒
            breathPhase = 0
            repeat(4) { seconds = 4 - it; kotlinx.coroutines.delay(1000) }
            // 屏息 4秒
            breathPhase = 1
            repeat(4) { seconds = 4 - it; kotlinx.coroutines.delay(1000) }
            // 呼气 4秒
            breathPhase = 2
            repeat(4) { seconds = 4 - it; kotlinx.coroutines.delay(1000) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "深呼吸练习",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "跟着圆圈的节奏呼吸",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        val primaryColor = MaterialTheme.colorScheme.primary

        // 呼吸动画圈
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().scale(breathScale)) {
                val circleColor = primaryColor.copy(alpha = alpha)
                drawCircle(
                    color = circleColor,
                    radius = size.minDimension / 2,
                    center = center
                )
            }
            Text(
                text = when (breathPhase) {
                    0 -> "吸气"
                    1 -> "屏息"
                    else -> "呼气"
                },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${seconds}秒",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onComplete) {
            Text("跳过呼吸练习 →", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DistractionList(onNext: () -> Unit) {
    val distractions = listOf(
        "立刻做20个俯卧撑" to "快速消耗体力，转移注意力",
        "用冷水洗脸" to "冷水刺激能瞬间唤醒理性思维",
        "给家人或朋友打个电话" to "社交连接能有效减少孤独感",
        "听一首喜欢的音乐" to "音乐能迅速改变情绪状态",
        "出门散步10分钟" to "换个环境，远离诱惑源",
        "大声朗读一段止观名言" to "用声音强化内心的力量",
        "写下此刻的感受" to "把冲动写出来，它就失去了力量",
        "做一组深呼吸" to "深沉呼吸，让心回归平静"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(20.dp)
    ) {
        Text(
            text = "分散注意力",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "立刻做以下任一件事，打断冲动",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        distractions.forEach { (action, reason) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = action,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = reason,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onNext) {
            Text("下一步 →", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun MotivationalQuotes(onNext: () -> Unit) {
    val quotes = listOf(
        "岂不闻天无绝人之路！只要我想走，路就在脚下。" to "路在脚下",
        "这世间没有绝境，只有对处境绝望的人。解决问题的答案就在我们自己的手中。" to "无绝境",
        "知人者智，自知者明。胜人者有力，自胜者强。" to "老子",
        "养心莫善于寡欲。" to "孟子",
        "止观存内，邪不可干。" to "黄帝内经",
        "每一次战胜欲望，都是对灵魂的一次淬炼。" to "修心",
        "你现在的坚持，会成为未来的铠甲。" to "励志",
        "君子之道，暗然而日章。" to "中庸"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(20.dp)
    ) {
        Text(
            text = "止观名言",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "让古圣先贤的智慧给你力量",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        quotes.forEach { (quote, author) ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = quote,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "—— $author",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onNext) {
            Text("回到呼吸练习 →", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun SuccessResistedScreen(
    modifier: Modifier = Modifier,
    abstinenceManager: AbstinenceManager,
    onBack: () -> Unit
) {
    val days = abstinenceManager.getCurrentStreak()
    val resistedCount = abstinenceManager.resistedCount

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 成功动画
        val scale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
            label = "success"
        )
        var started by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { started = true }

        Box(
            modifier = Modifier
                .size(100.dp)
                .scale(if (started) scale else 0.3f)
                .clip(CircleShape)
                .background(StatusGreen),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Shield,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "太棒了！",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "你成功抵抗了一次诱惑",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 数据统计
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("当前戒色", "${days}天", Icons.Default.LocalFireDepartment)
            StatItem("成功抵抗", "${resistedCount}次", Icons.Default.Shield)
            StatItem("最长记录", "${abstinenceManager.longestStreak}天", Icons.Default.EmojiEvents)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("返回首页", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun StatItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}