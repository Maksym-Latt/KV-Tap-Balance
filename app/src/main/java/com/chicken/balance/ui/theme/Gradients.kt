package com.chicken.balance.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppGradients {
    val primaryButton = Brush.verticalGradient(listOf(ButtonBlue, ButtonBlueDark))
    val secondaryButton = Brush.verticalGradient(listOf(ButtonOrange, ButtonOrangeDark))
    val panel = Brush.verticalGradient(listOf(PanelBlue, PanelBlue.copy(alpha = 0.92f)))
    val textFill = Brush.verticalGradient(listOf(TextWhite, TextWhite.copy(alpha = 0.82f)))
    val textStroke = Brush.verticalGradient(listOf(TextStroke, TextStroke.copy(alpha = 0.84f)))
    val disabledText = Brush.verticalGradient(listOf(Color.LightGray, Color.LightGray.copy(alpha = 0.7f)))
    val wind = Brush.verticalGradient(listOf(Color(0xFF8FD5F1), Color(0xFF5BAED8)))
    val sky = Brush.verticalGradient(colors = listOf(SkyTop, SkyBottom))
}
