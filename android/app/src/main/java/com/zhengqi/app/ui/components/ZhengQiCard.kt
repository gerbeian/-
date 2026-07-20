package com.zhengqi.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zhengqi.app.ui.theme.HairlineSoft

/**
 * Store/utility card — Apple's `store-utility-card` token.
 * White canvas, 1px hairline border, rounded.lg (18dp), 24dp padding.
 * No shadow — elevation comes from surface color change.
 */
@Composable
fun ZhengQiCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    cornerRadius: Dp = 18.dp,
    showBorder: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Column(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (showBorder) Modifier.border(
                    width = 1.dp,
                    color = HairlineSoft,
                    shape = shape
                ) else Modifier
            )
            .padding(contentPadding),
        content = content
    )
}

/**
 * Frosted-glass card — Apple's `sub-nav-frosted` / `floating-sticky-bar` aesthetic.
 * Parchment canvas at 72% alpha, backdrop blur, rounded.lg.
 */
@Composable
fun FrostedGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 18.dp,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.72f))
            .padding(contentPadding),
        content = content
    )
}
