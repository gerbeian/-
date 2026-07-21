package com.zhengqi.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhengqi.app.ui.components.LockPatternView
import com.zhengqi.app.ui.components.TrackItemIcons
import com.zhengqi.app.ui.components.ZhengQiButton
import com.zhengqi.app.ui.theme.HairlineSoft
import com.zhengqi.app.ui.theme.StatusGreen
import com.zhengqi.app.viewmodel.LockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockSetupScreen(
    onBack: () -> Unit,
    viewModel: LockViewModel = viewModel()
) {
    val isLockEnabled by viewModel.isLockEnabled.collectAsState()
    val lockType by viewModel.lockType.collectAsState()

    var selectedLockType by remember { mutableStateOf(lockType) }
    var digitPassword by remember { mutableStateOf("") }
    var confirmDigitPassword by remember { mutableStateOf("") }
    var setupStep by remember { mutableStateOf(0) }
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "密码锁设置",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
            Spacer(modifier = Modifier.height(24.dp))

            // Enable/Disable toggle — pearl capsule
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(17.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "启用密码锁",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isLockEnabled) "已启用" else "已关闭",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isLockEnabled) StatusGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isLockEnabled,
                    onCheckedChange = { enabled ->
                        viewModel.setLockEnabled(enabled)
                        if (!enabled) {
                            viewModel.disableLock()
                            setupStep = 0
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            if (isLockEnabled) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "选择密码类型",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LockTypeCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Dialpad,
                        title = "数字密码",
                        selected = selectedLockType == "digit",
                        onClick = { selectedLockType = "digit" }
                    )
                    LockTypeCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.GridView,
                        title = "手势密码",
                        selected = selectedLockType == "pattern",
                        onClick = { selectedLockType = "pattern" }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                when (selectedLockType) {
                    "digit" -> {
                        if (setupStep == 0 && !viewModel.hasDigitPassword()) {
                            Text(
                                text = "设置数字密码（4-6位）",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = digitPassword,
                                onValueChange = {
                                    if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                                        digitPassword = it
                                    }
                                },
                                label = { Text("输入密码") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = confirmDigitPassword,
                                onValueChange = {
                                    if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                                        confirmDigitPassword = it
                                    }
                                },
                                label = { Text("确认密码") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            ZhengQiButton(
                                text = "保存密码",
                                onClick = {
                                    if (digitPassword.length >= 4 && digitPassword == confirmDigitPassword) {
                                        viewModel.setDigitPassword(digitPassword)
                                        viewModel.setLockType("digit")
                                        message = "密码设置成功"
                                        digitPassword = ""
                                        confirmDigitPassword = ""
                                    } else {
                                        message = "密码不匹配或长度不足4位"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(
                                text = "数字密码已设置",
                                style = MaterialTheme.typography.titleMedium,
                                color = StatusGreen
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            ZhengQiButton(
                                text = "重置密码",
                                onClick = {
                                    setupStep = 0
                                    digitPassword = ""
                                    confirmDigitPassword = ""
                                },
                                modifier = Modifier.fillMaxWidth(),
                                variant = com.zhengqi.app.ui.components.ButtonVariant.Secondary
                            )
                        }
                    }
                    "pattern" -> {
                        if (setupStep == 0 && !viewModel.hasPatternPassword()) {
                            Text(
                                text = "设置手势密码（至少连接4个点）",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LockPatternView(
                                onPatternComplete = { pattern ->
                                    viewModel.setPatternPassword(pattern)
                                    viewModel.setLockType("pattern")
                                    message = "手势密码设置成功"
                                }
                            )
                        } else {
                            Text(
                                text = "手势密码已设置",
                                style = MaterialTheme.typography.titleMedium,
                                color = StatusGreen
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            ZhengQiButton(
                                text = "重置手势密码",
                                onClick = {
                                    viewModel.clearPatternPassword()
                                    setupStep = 0
                                },
                                modifier = Modifier.fillMaxWidth(),
                                variant = com.zhengqi.app.ui.components.ButtonVariant.Secondary
                            )
                        }
                    }
                }

                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (message.contains("成功")) StatusGreen else MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun LockTypeCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "lockTypePress"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(MaterialTheme.shapes.medium)
            .background(if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
            .then(
                if (!selected) Modifier.border(1.dp, HairlineSoft, MaterialTheme.shapes.medium)
                else Modifier
            )
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(17.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
