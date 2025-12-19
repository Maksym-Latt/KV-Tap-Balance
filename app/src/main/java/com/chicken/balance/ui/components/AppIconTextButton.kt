package com.chicken.balance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.balance.ui.theme.AppGradients

@Composable
fun AppIconTextButton(
    text: String,
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconSize: Dp = 32.dp,
    borderWidth: Dp = 2.dp,
    borderColor: Color = Color.Black,
    background: Brush = Brush.verticalGradient(
        listOf(
            Color(0xff85f6eb),
            Color(0xff378b81)
        )
    ),
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(borderWidth, borderColor, shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
                tint = Color(0xFFEAF3FF)
            )

            OutlineText(
                text = text,
                fillBrush = if (enabled) AppGradients.textFill else AppGradients.disabledText,
                outlineBrush = AppGradients.textStroke,
                fontSize = 30.sp
            )
        }
    }
}