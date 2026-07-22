package com.zhiguan.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = CanvasWhite,
    primaryContainer = PrimaryBlue.copy(alpha = 0.12f),
    onPrimaryContainer = PrimaryBlue,
    secondary = PrimaryFocus,
    onSecondary = CanvasWhite,
    secondaryContainer = PrimaryFocus.copy(alpha = 0.12f),
    onSecondaryContainer = PrimaryFocus,
    tertiary = PrimaryOnDark,
    onTertiary = CanvasWhite,
    background = CanvasWhite,
    onBackground = Ink,
    surface = CanvasWhite,
    onSurface = Ink,
    surfaceVariant = CanvasParchment,
    onSurfaceVariant = InkMuted80,
    surfaceTint = PrimaryBlue,
    outline = Hairline,
    outlineVariant = DividerSoft,
    error = SystemRed,
    onError = CanvasWhite,
    errorContainer = SystemRed.copy(alpha = 0.10f),
    onErrorContainer = SystemRed,
    scrim = SurfaceBlack
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryOnDark,
    onPrimary = SurfaceBlack,
    primaryContainer = SurfaceTile1,
    onPrimaryContainer = BodyOnDark,
    secondary = PrimaryOnDark,
    onSecondary = SurfaceBlack,
    tertiary = PrimaryBlue,
    onTertiary = CanvasWhite,
    background = SurfaceBlack,
    onBackground = BodyOnDark,
    surface = SurfaceTile1,
    onSurface = BodyOnDark,
    surfaceVariant = SurfaceTile2,
    onSurfaceVariant = BodyMuted,
    surfaceTint = PrimaryOnDark,
    outline = SurfaceTile3,
    outlineVariant = BodyMuted.copy(alpha = 0.20f),
    error = SystemRed,
    onError = CanvasWhite,
    errorContainer = SystemRed.copy(alpha = 0.18f),
    onErrorContainer = BodyOnDark,
    scrim = SurfaceBlack
)

@Composable
fun ZhiGuanTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !useDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ZhiGuanTypography,
        shapes = ZhiGuanShapes,
        content = content
    )
}
