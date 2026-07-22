package com.zhiguan.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhiguan.app.ui.components.LockPatternView
import com.zhiguan.app.viewmodel.LockViewModel

@Composable
fun LockScreen(
    onUnlock: () -> Unit,
    viewModel: LockViewModel = viewModel()
) {
    val lockType by viewModel.lockType.collectAsState()

    if (lockType == "pattern") {
        PatternLockScreen(onUnlock = onUnlock, viewModel = viewModel)
    } else {
        DigitLockScreen(onUnlock = onUnlock, viewModel = viewModel)
    }
}

@Composable
private fun DigitLockScreen(
    onUnlock: () -> Unit,
    viewModel: LockViewModel
) {
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }
    val shakeAnim = remember { Animatable(0f) }

    LaunchedEffect(error) {
        if (error) {
            shakeAnim.snapTo(30f)
            shakeAnim.animateTo(
                targetValue = 0f,
                animationSpec = spring(dampingRatio = 0.3f, stiffness = 1000f)
            )
            error = false
            password = ""
        }
    }

    val maxDigits = 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "请输入密码",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Password dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.graphicsLayer { translationX = shakeAnim.value }
        ) {
            repeat(maxDigits) { index ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(if (index < password.length) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (error) {
            Text(
                text = "密码错误，请重试",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Number pad — Apple button-icon-circular aesthetic
        val numbers = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("", "0", "⌫")
        )

        numbers.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { num ->
                    if (num.isEmpty()) {
                        Spacer(modifier = Modifier.size(64.dp))
                    } else if (num == "⌫") {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .clickable {
                                    if (password.isNotEmpty()) {
                                        password = password.dropLast(1)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Backspace,
                                contentDescription = "删除",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    } else {
                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()
                        val scale by animateFloatAsState(
                            targetValue = if (isPressed) 0.95f else 1f,
                            animationSpec = spring(dampingRatio = 0.5f, stiffness = 600f),
                            label = "numPress"
                        )
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .scale(scale)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                                .clickable(interactionSource = interactionSource, indication = null) {
                                    if (password.length < maxDigits) {
                                        password += num
                                        if (password.length == maxDigits) {
                                            if (viewModel.verifyDigitPassword(password)) {
                                                viewModel.unlock()
                                                onUnlock()
                                            } else {
                                                error = true
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = num,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun PatternLockScreen(
    onUnlock: () -> Unit,
    viewModel: LockViewModel
) {
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Lock,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "请绘制手势密码",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(32.dp))

        LockPatternView(
            onPatternComplete = { pattern ->
                if (viewModel.verifyPatternPassword(pattern)) {
                    viewModel.unlock()
                    onUnlock()
                } else {
                    error = true
                }
            },
            errorState = error
        )

        if (error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "手势错误，请重试",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}
