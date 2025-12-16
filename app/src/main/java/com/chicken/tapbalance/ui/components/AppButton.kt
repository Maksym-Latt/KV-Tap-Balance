package com.chicken.tapbalance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chicken.tapbalance.ui.theme.ButtonBlue
import com.chicken.tapbalance.ui.theme.ButtonBlueDark
import com.chicken.tapbalance.ui.theme.TextStroke
import com.chicken.tapbalance.ui.theme.TextWhite

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .shadow(6.dp, shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(ButtonBlue, ButtonBlueDark)
                ),
                shape
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlineText(
            text = text,
            color = if (enabled) TextWhite else Color.LightGray,
            outlineColor = TextStroke,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}
