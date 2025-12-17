package com.chicken.tapbalance.ui.menu

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.R
import com.chicken.tapbalance.ui.components.AppButton
import com.chicken.tapbalance.ui.components.Fence
import com.chicken.tapbalance.ui.components.FenceHeight
import com.chicken.tapbalance.ui.components.OutlineText
import com.chicken.tapbalance.ui.components.SkyBackground

@Composable
fun MenuScreen(
        state: kotlinx.coroutines.flow.StateFlow<MenuUiState>,
        onPlay: () -> Unit,
        onSkins: () -> Unit,
        onMusicToggle: (Boolean) -> Unit,
        onSfxToggle: (Boolean) -> Unit
) {
    val uiState by state.collectAsState()
    var showSettings by remember { mutableStateOf(false) }

    SkyBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .padding(bottom = FenceHeight - 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TopBar(uiState)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TitleLogo()
                    Spacer(modifier = Modifier.height(16.dp))
                    ChickenPreview()
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppButton(text = "Play", modifier = Modifier.fillMaxWidth(0.8f)) { onPlay() }
                    AppButton(text = "Skins", modifier = Modifier.fillMaxWidth(0.7f)) { onSkins() }
                    AppButton(text = "Settings", modifier = Modifier.fillMaxWidth(0.7f)) {
                        showSettings = true
                    }
                }
            }

            Fence(modifier = Modifier.align(Alignment.BottomCenter))
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
}

@Composable
private fun TitleLogo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlineText(
            text = "CHICKEN",
            fontSize = 36.sp
        )
        OutlineText(
            text = "TAP",
            fontSize = 36.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        OutlineText(
            text = "BALANCE",
            fontSize = 36.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ChickenPreview() {
    val infinite = rememberInfiniteTransition(label = "tilt")
    val angle by
            infinite.animateFloat(
                    initialValue = -6f,
                    targetValue = 6f,
                    animationSpec =
                            infiniteRepeatable(
                                    animation = tween(900, easing = LinearEasing),
                                    repeatMode = RepeatMode.Reverse
                            ),
                    label = "tiltAnim"
            )
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(
                painter = painterResource(id = R.drawable.chicken_1_calm),
                contentDescription = null,
                modifier = Modifier.size(160.dp).rotate(angle),
                contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun TopBar(uiState: MenuUiState) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End, // Content to the end (right)
            verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.End) {
            OutlineText(
                text = "Best: ${(uiState.bestScoreMs / 1000.0).formatSeconds()}s",
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
            OutlineText(
                text = "Points: ${uiState.points}",
                fontSize = 16.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

private fun Double.formatSeconds(): String = String.format("%.1f", this)
