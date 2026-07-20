package com.zhengqi.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Apple design ladder: 300 / 400 / 600 / 700 (500 deliberately absent)
// Display sizes use weight 600 with negative letter-spacing.
// Body copy at 17sp (not 16sp), line-height 1.47, letter-spacing -0.374px.
val ZhengQiTypography = Typography(
    // hero-display: 56px / 600 / 1.07 / -0.28px
    displayLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.374).sp
    ),
    // display-lg: 40px / 600 / 1.10 / 0
    displayMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.374).sp
    ),
    // display-md: 34px / 600 / 1.47 / -0.374px
    displaySmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.196.sp
    ),
    // lead: 28px / 400 / 1.14 / 0.196px
    headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // tagline: 21px / 600 / 1.19 / 0.231px
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 21.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.231.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 23.sp,
        letterSpacing = (-0.224).sp
    ),
    // body-strong: 17px / 600 / 1.24 / -0.374px
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.374).sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.224).sp
    ),
    // caption-strong: 14px / 600 / 1.29 / -0.224px
    titleSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.224).sp
    ),
    // body: 17px / 400 / 1.47 / -0.374px — the default paragraph
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 25.sp,
        letterSpacing = (-0.374).sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.224).sp
    ),
    // caption: 14px / 400 / 1.43 / -0.224px
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.224).sp
    ),
    // button-utility: 14px / 400 / 1.29 / -0.224px
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.224).sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 17.sp,
        letterSpacing = (-0.12).sp
    ),
    // fine-print: 12px / 400 / 1.0 / -0.12px
    labelSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 14.sp,
        letterSpacing = (-0.12).sp
    )
)
