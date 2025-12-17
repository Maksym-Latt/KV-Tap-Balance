package com.chicken.tapbalance.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun PauseOverlay(
    musicEnabled: Boolean,
    sfxEnabled: Boolean,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit
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
                text = "Paused",
                fontSize = 22.sp
            )
            ToggleRow("Music", musicEnabled) { onMusicToggle(!musicEnabled) }
            ToggleRow("Sounds", sfxEnabled) { onSfxToggle(!sfxEnabled) }
            AppButton(text = "Continue", modifier = Modifier.fillMaxWidth()) { onResume() }
            AppButton(text = "Restart", modifier = Modifier.fillMaxWidth()) { onRestart() }
            AppButton(text = "Menu", modifier = Modifier.fillMaxWidth()) { onMenu() }
        }
    }
}

@Composable
private fun ToggleRow(label: String, enabled: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlineText(
            text = label,
            fontSize = 16.sp
        )
        AppButton(
            text = if (enabled) "On" else "Off",
            modifier = Modifier.fillMaxWidth(0.4f)
        ) { onToggle() }
    }
}
