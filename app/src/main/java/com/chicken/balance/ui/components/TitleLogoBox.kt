package com.chicken.balance.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TitleLogoBox(modifier: Modifier = Modifier) {
    val chickenBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xff3c84df),
                Color(0xff1a457c)
            )
        )
    }
    val tapBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xff6ca9f1),
                Color(0xff2e70c5)
            )
        )
    }
    val balanceBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xff7ae7de),
                Color(0xff3e978d)
            )
        )
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        OutlineText(
            text = "CHICKEN",
            fontSize = 58.sp,
            fillBrush = chickenBrush,
            modifier = Modifier.offset(y = 8.dp)
        )
        OutlineText(
            text = "TAP",
            fontSize = 58.sp,
            fillBrush = tapBrush,
            modifier = Modifier
                .offset(y = 45.dp)
        )
        OutlineText(
            text = "BALANCE",
            fontSize = 58.sp,
            fillBrush = balanceBrush,
            modifier = Modifier
                .offset(y = 85.dp)
        )
    }
}