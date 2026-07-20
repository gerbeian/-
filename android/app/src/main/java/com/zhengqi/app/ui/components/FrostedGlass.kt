package com.zhengqi.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Frosted-glass box — Apple's `sub-nav-frosted` / `floating-sticky-bar` aesthetic.
 * Translucent canvas (72% alpha), rounded.lg, with optional backdrop blur applied by caller.
 */
@Composable
fun FrostedGlassBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp,
    blurRadius: Float = 15f,
    backgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape)
            .padding(17.dp),
        content = content
    )
}
