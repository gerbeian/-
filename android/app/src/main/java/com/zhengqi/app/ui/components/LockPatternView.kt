package com.zhengqi.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sqrt

data class LockPoint(val row: Int, val col: Int, val number: Int)

@Composable
fun LockPatternView(
    onPatternComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorState: Boolean = false
) {
    val points = remember {
        listOf(
            LockPoint(1, 1, 1), LockPoint(1, 2, 2), LockPoint(1, 3, 3),
            LockPoint(2, 1, 4), LockPoint(2, 2, 5), LockPoint(2, 3, 6),
            LockPoint(3, 1, 7), LockPoint(3, 2, 8), LockPoint(3, 3, 9)
        )
    }

    var selectedPoints by remember { mutableStateOf<List<LockPoint>>(emptyList()) }
    val lineColor = if (errorState) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val innerDotColor = if (errorState) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val rows = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9)
        )
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { num ->
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()
                    val scale by animateFloatAsState(
                        targetValue = if (isPressed) 0.85f else 1f,
                        animationSpec = spring(dampingRatio = 0.5f, stiffness = 600f),
                        label = "pointPress"
                    )
                    val isSelected = selectedPoints.any { it.number == num }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .scale(scale)
                            .background(
                                if (isSelected) lineColor else MaterialTheme.colorScheme.outlineVariant,
                                CircleShape
                            )
                            .clickable(interactionSource = interactionSource, indication = null) {
                                val point = points.first { it.number == num }
                                if (!selectedPoints.contains(point)) {
                                    selectedPoints = selectedPoints + point
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(innerDotColor, CircleShape)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Pattern completion
    LaunchedEffect(selectedPoints.size) {
        if (selectedPoints.size >= 4) {
            val pattern = selectedPoints.joinToString("") { it.number.toString() }
            onPatternComplete(pattern)
            selectedPoints = emptyList()
        }
    }
}
