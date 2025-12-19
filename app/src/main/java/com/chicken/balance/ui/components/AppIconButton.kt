package com.chicken.balance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun AppIconButton(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 58.dp,
    iconSize: Dp = 32.dp,
    borderWidth: Dp = 2.dp,
    borderColor: Color = Color.Black,
    background: Brush = Brush.verticalGradient(
        listOf(
            Color(0xff7cb6fc),
            Color(0xff2468be)
        )
    ),
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

    Box(
        modifier = modifier
            .size(width = size * 1.15f, height = size)
            .clip(shape)
            .background(background)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = Color(0xFFEAF3FF)
        )
    }
}
