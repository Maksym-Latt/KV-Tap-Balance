package com.chicken.tapbalance.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.ui.components.AppButton
import com.chicken.tapbalance.ui.components.OutlineText
import com.chicken.tapbalance.ui.theme.AppGradients
import com.chicken.tapbalance.ui.theme.OverlayDim


@Composable
fun GameOverOverlay(
    state: GameUiState,
    onTryAgain: () -> Unit,
    onMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OverlayDim),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(AppGradients.panel, RoundedCornerShape(18.dp))
                .padding(horizontal = 22.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlineText(
                text = "YOU LOST\nBALANCE!",
                fontSize = 26.sp,
                maxLines = 2
            )
            OutlineText(
                text = "Survival Time: ${(state.survivalTimeMs / 1000.0).formatSeconds()}s",
                fontSize = 18.sp
            )
            OutlineText(
                text = "Best: ${(state.bestScoreMs / 1000.0).formatSeconds()}s",
                fontSize = 18.sp
            )
            AppButton(text = "Try Again", modifier = Modifier.fillMaxWidth()) { onTryAgain() }
            AppButton(text = "Back to Menu", modifier = Modifier.fillMaxWidth()) { onMenu() }
        }
    }
}
