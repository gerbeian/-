package com.zhiguan.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Apple border-radius scale:
// none 0 · xs 5 · sm 8 · md 11 · lg 18 · pill/full 9999
val ZhiGuanShapes = Shapes(
    extraSmall = RoundedCornerShape(5.dp),   // rounded.xs
    small = RoundedCornerShape(8.dp),        // rounded.sm — utility buttons, inline imagery
    medium = RoundedCornerShape(11.dp),      // rounded.md — pearl capsules
    large = RoundedCornerShape(18.dp),       // rounded.lg — utility cards, accessories grid
    extraLarge = RoundedCornerShape(28.dp)   // large surfaces
)

// Pill / Full — used for primary CTAs, search input, configurator chips
val PillShape = RoundedCornerShape(9999.dp)
val CircularShape = RoundedCornerShape(9999.dp)
