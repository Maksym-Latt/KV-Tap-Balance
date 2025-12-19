package com.chicken.balance.ui.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chicken.balance.R
import com.chicken.balance.ui.components.SkyBackground
import com.chicken.balance.ui.components.TitleLogoBox
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onFinished()
    }

    SkyBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            SplashBackgroundLayer(
                chicken = { SplashChicken() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1.2f))

                TitleLogoBox(
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(2.2f))
            }
        }
    }
}

@Composable
private fun SplashBackgroundLayer(
    chicken: @Composable () -> Unit
) {
    var fenceTopYPx by remember { mutableIntStateOf(-1) }
    var chickenHeightPx by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fence),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .onGloballyPositioned { coords ->
                    fenceTopYPx = coords.positionInParent().y.roundToInt()
                },
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        if (fenceTopYPx >= 0) {
            val chickenOffsetY = fenceTopYPx - chickenHeightPx

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset { IntOffset(0, chickenOffsetY) }
                    .onSizeChanged { chickenHeightPx = it.height },
                contentAlignment = Alignment.BottomCenter
            ) {
                chicken()
            }
        }
    }
}

@Composable
private fun SplashChicken(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "tilt")
    val angle by infinite.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tiltAnim"
    )

    Image(
        painter = painterResource(id = R.drawable.chicken_1_calm),
        contentDescription = null,
        modifier = modifier
            .size(200.dp)
            .graphicsLayer {
                rotationZ = angle
                transformOrigin = TransformOrigin(0.5f, 1f)
            }
            .offset(y = 10.dp),
        contentScale = ContentScale.Fit
    )
}
