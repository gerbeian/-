package com.zhengqi.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
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
    var currentDragPos by remember { mutableStateOf<Offset?>(null) }
    val lineColor = if (errorState) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val innerDotColor = if (errorState) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val position = event.changes.firstOrNull()?.position ?: continue

                        when {
                            event.changes.firstOrNull()?.pressed == true -> {
                                // Find the closest dot within hit radius
                                val hitPoint = findClosestPoint(position, points, selectedPoints)
                                if (hitPoint != null) {
                                    selectedPoints = selectedPoints + hitPoint
                                }
                                currentDragPos = position
                            }
                            event.changes.firstOrNull()?.pressed == false -> {
                                // Finger lifted - complete pattern
                                if (selectedPoints.size >= 4) {
                                    val pattern = selectedPoints.joinToString("") { it.number.toString() }
                                    onPatternComplete(pattern)
                                }
                                selectedPoints = emptyList()
                                currentDragPos = null
                            }
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val spacing = 64.dp.toPx()
            val startX = (canvasWidth - spacing * 2) / 2
            val startY = (canvasHeight - spacing * 2) / 2

            // Build point positions map
            val pointPositions = points.map { point ->
                val x = startX + (point.col - 1) * spacing
                val y = startY + (point.row - 1) * spacing
                point.number to Offset(x, y)
            }.toMap()

            val outerRadius = 24.dp.toPx()
            val innerRadius = 8.dp.toPx()

            // Draw lines between selected points
            if (selectedPoints.size >= 2) {
                for (i in 0 until selectedPoints.size - 1) {
                    val from = pointPositions[selectedPoints[i].number]!!
                    val to = pointPositions[selectedPoints[i + 1].number]!!
                    drawLine(
                        color = lineColor,
                        start = from,
                        end = to,
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // Draw line from last selected point to current drag position
            if (selectedPoints.isNotEmpty() && currentDragPos != null) {
                val lastPos = pointPositions[selectedPoints.last().number]!!
                drawLine(
                    color = lineColor.copy(alpha = 0.6f),
                    start = lastPos,
                    end = currentDragPos!!,
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // Draw all dot circles
            pointPositions.forEach { (number, center) ->
                val isSelected = selectedPoints.any { it.number == number }
                drawCircle(
                    color = if (isSelected) lineColor else outlineColor,
                    radius = outerRadius,
                    center = center,
                    style = if (isSelected) Stroke(width = 3.dp.toPx()) else Stroke(width = 2.dp.toPx())
                )
                if (isSelected) {
                    drawCircle(
                        color = innerDotColor,
                        radius = innerRadius,
                        center = center
                    )
                }
            }
        }
    }
}

private fun findClosestPoint(
    position: Offset,
    points: List<LockPoint>,
    selectedPoints: List<LockPoint>,
    hitRadius: Float = 80f
): LockPoint? {
    // Approximate grid positions (3x3 within the padded area)
    val gridSize = 3
    val stepX = 64f
    val stepY = 64f

    val startX = 32f
    val startY = 32f

    var closest: LockPoint? = null
    var closestDist = Float.MAX_VALUE

    for (point in points) {
        if (selectedPoints.contains(point)) continue
        val cx = startX + (point.col - 1) * stepX
        val cy = startY + (point.row - 1) * stepY
        val dx = position.x - cx
        val dy = position.y - cy
        val dist = sqrt(dx * dx + dy * dy)
        if (dist < hitRadius && dist < closestDist) {
            closest = point
            closestDist = dist
        }
    }
    return closest
}