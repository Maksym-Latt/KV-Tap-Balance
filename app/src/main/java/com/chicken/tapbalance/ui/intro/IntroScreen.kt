package com.chicken.tapbalance.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.R
import com.chicken.tapbalance.ui.components.AppButton
import com.chicken.tapbalance.ui.components.AppIconButton
import com.chicken.tapbalance.ui.components.Fence
import com.chicken.tapbalance.ui.components.FenceHeight
import com.chicken.tapbalance.ui.components.OutlineText
import com.chicken.tapbalance.ui.components.SkyBackground
import com.chicken.tapbalance.ui.theme.AppGradients

@Composable
fun IntroScreen(
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    SkyBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 16.dp)
                    .padding(bottom = FenceHeight),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIconButton(
                        painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                        contentDescription = "Back",
                        onClick = onBack
                    )
                    OutlineText(
                        text = "How to play",
                        fontSize = 22.sp,
                        textAlign = TextAlign.End
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    IntroCard(
                        painter = painterResource(id = R.drawable.chicken_1_calm),
                        title = "Tap to balance",
                        subtitle = "Tap left or right to keep the chicken steady on the fence."
                    )
                    IntroCard(
                        painter = painterResource(id = R.drawable.ic_wind),
                        title = "Watch the wind",
                        subtitle = "Wind icons show push direction — adjust before it tips you.",
                        iconTint = null
                    )
                    IntroCard(
                        painter = painterResource(id = R.drawable.ic_leaf),
                        title = "Stay longer",
                        subtitle = "Survive as long as you can to stack points and unlock skins.",
                        iconTint = Color(0xFF6BB34F)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppButton(text = "Start balancing", modifier = Modifier.fillMaxWidth()) {
                        onStart()
                    }
                    OutlineText(
                        text = "Tilt beyond 15° and you fall — keep it steady!",
                        fontSize = 14.sp
                    )
                }
            }

            Fence(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun IntroCard(
    painter: Painter,
    title: String,
    subtitle: String,
    iconTint: Color? = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppGradients.panel, RoundedCornerShape(14.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(42.dp),
            colorFilter = iconTint?.let { ColorFilter.tint(it) },
            contentScale = ContentScale.Fit
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlineText(text = title, fontSize = 18.sp, textAlign = TextAlign.Start)
            OutlineText(text = subtitle, fontSize = 14.sp, textAlign = TextAlign.Start)
        }
    }
}
