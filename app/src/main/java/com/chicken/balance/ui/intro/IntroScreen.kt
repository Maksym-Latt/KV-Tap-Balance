package com.chicken.balance.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.chicken.balance.R
import com.chicken.balance.ui.components.AppButton
import com.chicken.balance.ui.components.AppIconButton
import com.chicken.balance.ui.components.Fence
import com.chicken.balance.ui.components.OutlineText
import com.chicken.balance.ui.components.SkyBackground
import com.chicken.balance.ui.theme.AppGradients

@Composable
fun IntroScreen(
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    SkyBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Fence(modifier = Modifier.align(Alignment.BottomCenter))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(horizontal = 22.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IntroCard(
                        painter = painterResource(id = R.drawable.chicken_1_calm),
                        title = "Tap to balance",
                        subtitle = "Tap left or right to keep the chicken steady on the fence.",
                        iconTint = null
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

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppButton(
                        text = "Start balancing",
                        modifier = Modifier.fillMaxWidth()
                    ) { onStart() }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
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

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlineText(text = title, fontSize = 18.sp, textAlign = TextAlign.Start)
            OutlineText(text = subtitle, fontSize = 14.sp, textAlign = TextAlign.Start)
        }
    }
}
