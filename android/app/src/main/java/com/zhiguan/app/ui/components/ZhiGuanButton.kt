package com.zhiguan.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zhiguan.app.ui.theme.PillShape
import com.zhiguan.app.ui.theme.StatusGray

enum class ButtonVariant {
    Primary, Secondary, Danger, DarkUtility
}

/**
 * Apple `button-primary` token — Action Blue pill, scale(0.95) on press.
 * Secondary: ghost pill with Action Blue text and 1px border.
 * Danger: SystemRed pill (reserved for destructive actions).
 * DarkUtility: ink fill, on-dark text, rounded.sm.
 */
@Composable
fun ZhiGuanButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 600f),
        label = "pressScale"
    )

    val (containerColor, contentColor, shape) = when (variant) {
        ButtonVariant.Primary -> Triple(
            if (enabled) MaterialTheme.colorScheme.primary else StatusGray,
            MaterialTheme.colorScheme.onPrimary,
            PillShape
        )
        ButtonVariant.Secondary -> Triple(
            Color.Transparent,
            if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            PillShape
        )
        ButtonVariant.Danger -> Triple(
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.onError,
            PillShape
        )
        ButtonVariant.DarkUtility -> Triple(
            MaterialTheme.colorScheme.onBackground,
            MaterialTheme.colorScheme.background,
            RoundedCornerShape(8.dp)
        )
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .scale(scale),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = StatusGray,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = null,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 11.dp),
        interactionSource = interactionSource,
        border = if (variant == ButtonVariant.Secondary && enabled) {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        } else null
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
