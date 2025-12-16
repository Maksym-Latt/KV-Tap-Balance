package com.chicken.tapbalance.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.ui.theme.PoppinsBold

@Composable
fun OutlineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    outlineColor: Color,
    fontSize: TextUnit = 24.sp,
    outlineWidth: Dp = 2.dp,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = Int.MAX_VALUE
) {
    val style = TextStyle(
        fontFamily = PoppinsBold,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        textAlign = textAlign
    )
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        listOf(
            Pair(outlineWidth, outlineWidth),
            Pair(-outlineWidth, outlineWidth),
            Pair(outlineWidth, -outlineWidth),
            Pair(-outlineWidth, -outlineWidth),
            Pair(outlineWidth, 0.dp),
            Pair(-outlineWidth, 0.dp),
            Pair(0.dp, outlineWidth),
            Pair(0.dp, -outlineWidth)
        ).forEach { (x, y) ->
            androidx.compose.material3.Text(
                text = text,
                style = style,
                color = outlineColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x, y),
                maxLines = maxLines
            )
        }
        androidx.compose.material3.Text(
            text = text,
            style = style,
            color = color,
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines
        )
    }
}
