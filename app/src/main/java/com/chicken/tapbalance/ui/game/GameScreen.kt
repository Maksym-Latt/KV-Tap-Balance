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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.R
import com.chicken.tapbalance.ui.components.AppIconButton
import com.chicken.tapbalance.ui.components.Fence
import com.chicken.tapbalance.ui.components.FenceHeight
import com.chicken.tapbalance.ui.components.OutlineText
import com.chicken.tapbalance.ui.components.SkyBackground
import com.chicken.tapbalance.ui.game.GameConstants.CHICKEN_BOTTOM_PADDING
import com.chicken.tapbalance.ui.theme.AppGradients
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

            GameContent(state = state, onPause = viewModel::pauseGame)

            ChickenOnFence(
                state = state,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = FenceHeight - 16.dp)
            )

            Fence(modifier = Modifier.align(Alignment.BottomCenter))

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
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .padding(bottom = FenceHeight + GameConstants.CHICKEN_BOTTOM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconButton(
                painter = rememberVectorPainter(image = Icons.Default.Pause),
                contentDescription = "Pause",
                onClick = onPause
            )
            Column(horizontalAlignment = Alignment.End) {
                OutlineText(
                    text = "Points: ${state.points}",
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
                OutlineText(
                    text = "Best: ${(state.bestScoreMs / 1000.0).formatSeconds()}s",
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
        }
        WindHint(state)
        TimerFooter(state)
    }
}

@Composable
private fun WindHint(state: GameUiState) {
    AnimatedVisibility(visible = state.isWindActive) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp)
                .background(
                    AppGradients.wind,
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconBadge(painter = painterResource(id = R.drawable.ic_wind))
            OutlineText(
                text = if (state.windDirection > 0) "Wind →" else "Wind ←",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun ChickenOnFence(state: GameUiState, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Image(
            painter = painterResource(
                id = if (abs(state.chickenAngle) > GameConstants.CHICKEN_TILT_FOR_CHANGE) state.currentSkin.fallRes else state.currentSkin.calmRes
            ),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .rotate(state.chickenAngle),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun TimerFooter(state: GameUiState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlineText(
            text = "Survival: ${(state.survivalTimeMs / 1000.0).formatSeconds()}s",
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlineText(
            text = "Tap left/right to balance!",
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
            Image(
                painter = painterResource(id = R.drawable.ic_leaf),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .rotate(leaf.rotation),
                colorFilter = ColorFilter.tint(Color(0xFF6BB34F))
            )
        }
    }
}

@Composable
private fun IconBadge(painter: Painter) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
}

private fun Double.formatSeconds(): String = String.format("%.2f", this)
