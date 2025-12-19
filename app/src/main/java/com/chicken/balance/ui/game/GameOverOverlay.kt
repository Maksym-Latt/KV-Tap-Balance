package com.chicken.balance.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.balance.ui.components.AppButton
import com.chicken.balance.ui.components.OutlineText
import com.chicken.balance.ui.theme.OverlayDim

@Composable
fun GameOverOverlay(state: GameUiState, onTryAgain: () -> Unit, onMenu: () -> Unit) {
        Box(
                modifier =
                        Modifier.fillMaxSize()
                                .background(OverlayDim)
                                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
                Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Spacer(modifier = Modifier.weight(1.15f))

                        OutlineText(text = "YOU LOST\nBALANCE!", fontSize = 44.sp, maxLines = 2)

                        Spacer(modifier = Modifier.weight(0.9f))

                        OutlineText(
                                text = "Survival Time: ${state.survivalTimeMs.toClock()}",
                                fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlineText(
                                text = "Points Earned: ${state.lastSessionPoints}",
                                fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.weight(0.8f))

                        Image(
                                painter = painterResource(id = state.currentSkin.fallRes),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(0.6f),
                                contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.weight(0.65f))

                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                        ) {
                                AppButton(
                                        text = "Try Again",
                                        modifier = Modifier.fillMaxWidth(0.58f),
                                        onClick = onTryAgain
                                )
                                AppButton(
                                        text = "Back to Menu",
                                        modifier = Modifier.fillMaxWidth(0.75f),
                                        onClick = onMenu
                                )
                        }

                        Spacer(modifier = Modifier.weight(0.55f))
                }
        }
}

private fun Long.toClock(): String {
        val totalSeconds = (this / 1000L).toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
}
