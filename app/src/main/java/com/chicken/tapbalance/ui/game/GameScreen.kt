package com.chicken.tapbalance.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.R
import com.chicken.tapbalance.ui.components.AppButton
import com.chicken.tapbalance.ui.components.Fence
import com.chicken.tapbalance.ui.components.OutlineText
import com.chicken.tapbalance.ui.components.SkyBackground
import com.chicken.tapbalance.ui.theme.OverlayDim
import com.chicken.tapbalance.ui.theme.PanelBlue
import com.chicken.tapbalance.ui.theme.TextStroke
import com.chicken.tapbalance.ui.theme.TextWhite
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.abs

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackToMenu: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.startGame() }

    SkyBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val centerX = size.width / 2
                        if (offset.x < centerX) viewModel.onLeftTap() else viewModel.onRightTap()
                    }
                }
        ) {
            LeavesLayer(state)
            GameContent(state, viewModel::pauseGame)
            if (state.isPaused) {
                PauseOverlay(
                    musicEnabled = state.musicEnabled,
                    sfxEnabled = state.sfxEnabled,
                    onResume = viewModel::resumeGame,
                    onRestart = viewModel::restartGame,
                    onMenu = {
                        viewModel.startIdle()
                        onBackToMenu()
                    },
                    onMusicToggle = viewModel::onToggleMusic,
                    onSfxToggle = viewModel::onToggleSfx
                )
            }
            if (state.isGameOver) {
                GameOverOverlay(
                    state = state,
                    onTryAgain = viewModel::restartGame,
                    onMenu = {
                        viewModel.startIdle(); onBackToMenu()
                    }
                )
            }
        }
    }
}

@Composable
private fun GameContent(state: GameUiState, onPause: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPause) {
                Icon(Icons.Default.Pause, contentDescription = "Pause", tint = Color.White)
            }
            Column(horizontalAlignment = Alignment.End) {
                OutlineText(
                    text = "Points: ${state.points}",
                    color = TextWhite,
                    outlineColor = TextStroke,
                    fontSize = 16.sp
                )
                OutlineText(
                    text = "Best: ${(state.bestScoreMs / 1000.0).formatSeconds()}s",
                    color = TextWhite,
                    outlineColor = TextStroke,
                    fontSize = 16.sp
                )
            }
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            WindHint(state)
            ChickenOnFence(state)
        }
        TimerFooter(state)
    }
}

@Composable
private fun WindHint(state: GameUiState) {
    AnimatedVisibility(visible = state.isWindActive) {
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF7CC9EC), Color(0xFF4BA9DA))
                    ),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            OutlineText(
                text = if (state.windDirection > 0) "Wind →" else "Wind ←",
                color = TextWhite,
                outlineColor = TextStroke,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ChickenOnFence(state: GameUiState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(
                id = if (abs(state.chickenAngle) > 35f) state.currentSkin.fallRes else state.currentSkin.calmRes
            ),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .rotate(state.chickenAngle),
            contentScale = ContentScale.Fit
        )
        Fence(modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun TimerFooter(state: GameUiState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlineText(
            text = "Survival: ${(state.survivalTimeMs / 1000.0).formatSeconds()}s",
            color = TextWhite,
            outlineColor = TextStroke,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlineText(
            text = "Tap left/right to balance!",
            color = TextWhite,
            outlineColor = TextStroke,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}


@Composable
private fun LeavesLayer(state: GameUiState) {
    state.leaves.forEach { leaf ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = (leaf.xFraction * 280).dp,
                    top = (leaf.yFraction * 500).dp
                )
        ) {
            Surface(
                modifier = Modifier.size(12.dp),
                color = Color(0xFF8BC34A),
                shape = RoundedCornerShape(50)
            ) {}
        }
    }
}

private fun Double.formatSeconds(): String = String.format("%.2f", this)
