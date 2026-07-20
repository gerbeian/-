package com.zhengqi.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zhengqi.app.data.model.TrackItem
import com.zhengqi.app.ui.components.TrackItemIcons
import com.zhengqi.app.ui.components.ZhengQiButton
import com.zhengqi.app.ui.theme.HairlineSoft
import com.zhengqi.app.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackItemConfigScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val trackItems by viewModel.trackItems.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<TrackItem?>(null) }
    var newName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("check") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "追踪项配置",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 17.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trackItems) { item ->
                TrackItemRow(
                    trackItem = item,
                    onEdit = { editingItem = item },
                    onDelete = {
                        if (!item.isDefault) {
                            viewModel.deleteTrackItem(item.id)
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                ZhengQiButton(
                    text = "添加追踪项",
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Add dialog
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    "添加追踪项",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("名称") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "选择图标",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IconPickerGrid(
                        selectedIcon = selectedIcon,
                        onIconSelected = { selectedIcon = it }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.addTrackItem(newName, selectedIcon)
                            newName = ""
                            selectedIcon = "check"
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("添加", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("取消", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    // Edit dialog
    editingItem?.let { item ->
        var editName by remember(item.id) { mutableStateOf(item.name) }
        var editIcon by remember(item.id) { mutableStateOf(item.iconName) }

        AlertDialog(
            onDismissRequest = { editingItem = null },
            title = {
                Text(
                    "编辑追踪项",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("名称") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    IconPickerGrid(
                        selectedIcon = editIcon,
                        onIconSelected = { editIcon = it }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editName.isNotBlank()) {
                            viewModel.updateTrackItem(
                                item.copy(name = editName, iconName = editIcon)
                            )
                            editingItem = null
                        }
                    }
                ) {
                    Text("保存", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingItem = null }) {
                    Text("取消", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
}

@Composable
private fun IconPickerGrid(
    selectedIcon: String,
    onIconSelected: (String) -> Unit
) {
    // Show first 5 options in a single row
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TrackItemIcons.options.take(5).forEach { (icon, _) ->
            val vector = TrackItemIcons.fromName(icon)
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.92f else 1f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 600f),
                label = "iconPress"
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .scale(scale)
                    .clip(MaterialTheme.shapes.small)
                    .background(if (selectedIcon == icon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                    .clickable(interactionSource = interactionSource, indication = null) {
                        onIconSelected(icon)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    vector,
                    contentDescription = null,
                    tint = if (selectedIcon == icon) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun TrackItemRow(
    trackItem: TrackItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val icon = TrackItemIcons.fromName(trackItem.iconName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, HairlineSoft, MaterialTheme.shapes.medium)
            .padding(horizontal = 17.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = trackItem.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (trackItem.isDefault) {
                Text(
                    text = "默认项",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "编辑", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (!trackItem.isDefault) {
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
