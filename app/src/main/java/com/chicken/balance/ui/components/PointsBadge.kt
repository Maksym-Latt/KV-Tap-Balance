package com.chicken.balance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PointsBadge(
    points: Int,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(26.dp)

    Box(
        modifier = modifier
            .border(2.dp, Color.Black, shape)
            .background(Color.White.copy(alpha = 0.10f), shape)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlineText(
            text = "Points: $points",
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}