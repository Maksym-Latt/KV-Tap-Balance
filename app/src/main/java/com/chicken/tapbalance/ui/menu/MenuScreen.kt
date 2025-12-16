package com.chicken.tapbalance.ui.menu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.R
import com.chicken.tapbalance.ui.components.AppButton
import com.chicken.tapbalance.ui.components.Fence
import com.chicken.tapbalance.ui.components.OutlineText
import com.chicken.tapbalance.ui.components.SkyBackground
import com.chicken.tapbalance.ui.theme.ButtonOrange
import com.chicken.tapbalance.ui.theme.ButtonOrangeDark
import com.chicken.tapbalance.ui.theme.TextStroke
import com.chicken.tapbalance.ui.theme.TextWhite

@Composable
fun MenuScreen(
    state: kotlinx.coroutines.flow.StateFlow<MenuUiState>,
    onPlay: () -> Unit,
    onSkins: () -> Unit,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit
) {
    val uiState by state.collectAsState()
    SkyBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TopBar(uiState, onMusicToggle, onSfxToggle)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TitleLogo()
                    Spacer(modifier = Modifier.height(16.dp))
                    ChickenPreview()
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppButton(text = "Skins", modifier = Modifier.fillMaxWidth(0.7f)) { onSkins() }
                    AppButton(text = "Play", modifier = Modifier.fillMaxWidth(0.8f)) { onPlay() }
                }
                Fence(modifier = Modifier.padding(bottom = 8.dp))
            }
        }
    }
}

@Composable
private fun TitleLogo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlineText(
            text = "CHICKEN",
            color = TextWhite,
            outlineColor = TextStroke,
            fontSize = 36.sp
        )
        OutlineText(
            text = "TAP",
            color = TextWhite,
            outlineColor = TextStroke,
            fontSize = 36.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        OutlineText(
            text = "BALANCE",
            color = TextWhite,
            outlineColor = TextStroke,
            fontSize = 36.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ChickenPreview() {
    val infinite = rememberInfiniteTransition(label = "tilt")
    val angle by infinite.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "tiltAnim"
    )
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.chicken_1_calm),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .rotate(angle),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun TopBar(
    uiState: MenuUiState,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SettingsPanel(
            musicEnabled = uiState.musicEnabled,
            sfxEnabled = uiState.sfxEnabled,
            onMusicToggle = onMusicToggle,
            onSfxToggle = onSfxToggle
        )
        Column(horizontalAlignment = Alignment.End) {
            OutlineText(
                text = "Best: ${(uiState.bestScoreMs / 1000.0).formatSeconds()}s",
                color = TextWhite,
                outlineColor = TextStroke,
                fontSize = 16.sp
            )
            OutlineText(
                text = "Points: ${uiState.points}",
                color = TextWhite,
                outlineColor = TextStroke,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun SettingsPanel(
    musicEnabled: Boolean,
    sfxEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                Brush.verticalGradient(listOf(ButtonOrange, ButtonOrangeDark)),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlineText(
                text = "Music",
                color = TextWhite,
                outlineColor = TextStroke,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            ToggleCircle(enabled = musicEnabled) { onMusicToggle(!musicEnabled) }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlineText(
                text = "Sounds",
                color = TextWhite,
                outlineColor = TextStroke,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            ToggleCircle(enabled = sfxEnabled) { onSfxToggle(!sfxEnabled) }
        }
    }
}

@Composable
private fun ToggleCircle(enabled: Boolean, onToggle: () -> Unit) {
    Box(
        modifier = Modifier
            .size(26.dp)
            .background(
                color = if (enabled) Color(0xFF5BDA7A) else Color(0xFFC0C0C0),
                shape = RoundedCornerShape(50)
            )
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        OutlineText(
            text = if (enabled) "ON" else "OFF",
            color = TextWhite,
            outlineColor = TextStroke,
            fontSize = 10.sp
        )
    }
}

private fun Double.formatSeconds(): String = String.format("%.1f", this)
