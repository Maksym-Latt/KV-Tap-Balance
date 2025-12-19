package com.chicken.balance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chicken.balance.ui.theme.AppGradients

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    background: Brush = Brush.verticalGradient(
        listOf(
            Color(0xff7cb6fc),
            Color(0xff2468be)
        )
    ),
    textBrush: Brush = AppGradients.textFill,
    borderWidth: Dp = 2.dp,
    borderColor: Color = Color.Black,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 28.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlineText(
            text = text,
            fillBrush = if (enabled) textBrush else AppGradients.disabledText,
            outlineBrush = AppGradients.textStroke,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}
