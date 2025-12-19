package com.chicken.balance.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.chicken.balance.R
import com.chicken.balance.ui.components.AppIconButton
import com.chicken.balance.ui.components.Fence
import com.chicken.balance.ui.components.OutlineText
import com.chicken.balance.ui.components.PointsBadge
import com.chicken.balance.ui.components.SkyBackground
import com.chicken.balance.ui.theme.AppGradients
import kotlin.math.abs

@Composable
fun GameScreen(viewModel: GameViewModel, onBackToMenu: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        if (!state.isGameOver) {
            viewModel.pauseGame()
        }
    }

    BackHandler(enabled = !state.isPaused) {
        if (state.isGameOver) {
            viewModel.startIdle()
            onBackToMenu()
        } else {
            viewModel.pauseGame()
        }
    }

    LaunchedEffect(Unit) { viewModel.startGame() }

    val density = LocalDensity.current
    var fenceHeightPx by remember { mutableIntStateOf(0) }

    val fenceHeightDp = remember(fenceHeightPx) { with(density) { fenceHeightPx.toDp() } }

    SkyBackground {
        Box(
                modifier =
                        Modifier.fillMaxSize().pointerInput(Unit) {
                            detectTapGestures { offset ->
                                val centerX = size.width / 2
                                if (offset.x < centerX) {
                                    viewModel.onLeftTap(offset.x, offset.y)
                                } else {
                                    viewModel.onRightTap(offset.x, offset.y)
                                }
                            }
                        }
        ) {
            LeavesLayer(state)
            RipplesLayer(state.ripples)

            GameContent(state = state, onPause = viewModel::pauseGame, bottomInset = fenceHeightDp)

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                Fence(
                        modifier =
                                Modifier.align(Alignment.BottomCenter).onSizeChanged {
                                    fenceHeightPx = it.height
                                }
                )

                ChickenOnFence(
                        state = state,
                        modifier =
                                Modifier.align(Alignment.BottomCenter)
                                        .offset(
                                                y = -fenceHeightDp
                                        ) // ставимо курочку рівно на верх забору
                                        .zIndex(1f)
                )
            }

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
                            viewModel.startIdle()
                            onBackToMenu()
                        }
                )
            }
        }
    }
}

@Composable
private fun GameContent(state: GameUiState, onPause: () -> Unit, bottomInset: Dp) {
    Column(
            modifier =
                    Modifier.fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .padding(bottom = bottomInset + GameConstants.CHICKEN_BOTTOM_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                    modifier =
                            Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.safeDrawing),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                AppIconButton(
                        painter = rememberVectorPainter(image = Icons.Default.Pause),
                        contentDescription = "Pause",
                        onClick = onPause
                )

                // Show Total accumulated points (Stored + Session)
                PointsBadge(points = state.points + state.sessionPoints)
            }

            Spacer(modifier = Modifier.height(16.dp))
            TimerFooter(state)
        }

        WindHint(state)

        // Removed TimerFooter from bottom
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun WindHint(state: GameUiState) {
    AnimatedVisibility(visible = state.isWindActive) {
        Row(
                modifier =
                        Modifier.padding(top = 12.dp)
                                .background(AppGradients.wind, RoundedCornerShape(12.dp))
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
        val isFalling = abs(state.chickenAngle) > GameConstants.CHICKEN_TILT_FOR_CHANGE
        val scaleX = if (isFalling && state.chickenAngle > 0) -1f else 1f

        Image(
                painter =
                        painterResource(
                                id =
                                        if (isFalling) state.currentSkin.fallRes
                                        else state.currentSkin.calmRes
                        ),
                contentDescription = null,
                modifier =
                        Modifier.size(180.dp)
                                .graphicsLayer {
                                    this.scaleX = scaleX
                                    this.rotationZ = state.chickenAngle
                                    this.transformOrigin = TransformOrigin(0.5f, 1f)
                                }
                                .offset(y = 10.dp),
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
        OutlineText(text = "Tap left/right to balance!", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun LeavesLayer(state: GameUiState) {
    state.leaves.forEach { leaf ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                    painter = painterResource(id = R.drawable.ic_leaf),
                    contentDescription = null,
                    modifier =
                            Modifier.offset(
                                            x = (leaf.xFraction * 320).dp, // Increased range
                                            y = (leaf.yFraction * 700).dp // Increased range
                                    )
                                    .size(38.dp) // Increased size
                                    .rotate(leaf.rotation),
                    colorFilter = ColorFilter.tint(Color(0xFF6BB34F))
            )
        }
    }
}

@Composable
fun RipplesLayer(ripples: List<RippleState>) {
    ripples.forEach { ripple -> RippleEffect(ripple) }
}

@Composable
fun RippleEffect(ripple: RippleState) {
    val transition = rememberInfiniteTransition()
    var time by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(ripple.id) {
        val startTime = System.currentTimeMillis()
        while (true) {
            val now = System.currentTimeMillis()
            val elapsed = now - startTime
            if (elapsed > 1000) break
            time = elapsed / 1000f
            withFrameMillis {}
        }
    }

    val alpha = (1f - time).coerceIn(0f, 1f)
    val scale = 0.5f + (time * 2f)

    if (alpha > 0) {
        Box(
                modifier =
                        Modifier.offset { IntOffset(ripple.x.toInt() - 50, ripple.y.toInt() - 50) }
                                .size(100.dp) // Base size
                                .scale(scale)
                                .border(2.dp, Color.White.copy(alpha = alpha * 0.5f), CircleShape)
                                .clip(CircleShape)
        )
    }
}

@Composable
private fun IconBadge(painter: Painter) {
    Box(
            modifier =
                    Modifier.size(32.dp)
                            .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
    ) { Image(painter = painter, contentDescription = null, modifier = Modifier.size(18.dp)) }
}

fun Double.formatSeconds(): String = String.format("%.2f", this)
