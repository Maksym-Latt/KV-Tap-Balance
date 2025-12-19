package com.chicken.balance.ui.menu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chicken.balance.R
import com.chicken.balance.ui.components.AppButton
import com.chicken.balance.ui.components.AppIconButton
import com.chicken.balance.ui.components.AppIconTextButton
import com.chicken.balance.ui.components.SkyBackground
import com.chicken.balance.ui.components.TitleLogoBox
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt

@Composable
fun MenuScreen(
    state: StateFlow<MenuUiState>,
    onPlay: () -> Unit,
    onSkins: () -> Unit,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit
) {
    val uiState by state.collectAsState()
    var showSettings by remember { mutableStateOf(false) }

    var fenceTopYPx by remember { mutableIntStateOf(-1) }
    var playHeightPx by remember { mutableIntStateOf(0) }

    SkyBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            MenuBackgroundLayer(
                onFenceTopY = { fenceTopYPx = it },
                chicken = { ChickenPreview() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    AppIconButton(
                        painter = rememberVectorPainter(Icons.Default.Settings),
                        contentDescription = "Settings",
                        onClick = { showSettings = true }
                    )
                }

                Spacer(modifier = Modifier.weight(0.5f))

                TitleLogoBox(
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.weight(2f))

                AppButton(
                    text = "Skins",
                    modifier = Modifier.width(140.dp),
                    onClick = onSkins
                )

                Spacer(modifier = Modifier.weight(0.5f))
                Spacer(modifier = Modifier.height(220.dp))
                Spacer(modifier = Modifier.weight(0.5f))

                AppIconTextButton(
                    text = "PLAY",
                    painter = rememberVectorPainter(Icons.Default.PlayArrow),
                    contentDescription = "Play",
                    modifier = Modifier.fillMaxWidth(0.65f),
                    onClick = onPlay
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    if (showSettings) {
        SettingsOverlay(
            musicEnabled = uiState.musicEnabled,
            sfxEnabled = uiState.sfxEnabled,
            onMusicToggle = onMusicToggle,
            onSfxToggle = onSfxToggle,
            onDismiss = { showSettings = false }
        )
    }
}


@Composable
private fun MenuBackgroundLayer(
    onFenceTopY: (Int) -> Unit,
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
                    val topY = coords.positionInParent().y.roundToInt()
                    fenceTopYPx = topY
                    onFenceTopY(topY)
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
private fun ChickenPreview(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "tilt")
    val angle by infinite.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
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
            .size(220.dp)
            .graphicsLayer {
                rotationZ = angle
                transformOrigin = TransformOrigin(0.5f, 1f)
            }
            .offset(y = 10.dp),
        contentScale = ContentScale.Fit
    )
}
