package com.chicken.tapbalance.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.ui.components.OutlineText
import com.chicken.tapbalance.ui.theme.ButtonOrange
import com.chicken.tapbalance.ui.theme.ButtonOrangeDark
import com.chicken.tapbalance.ui.theme.TextStroke
import com.chicken.tapbalance.ui.theme.TextWhite

@Composable
fun SettingsOverlay(
        musicEnabled: Boolean,
        sfxEnabled: Boolean,
        onMusicToggle: (Boolean) -> Unit,
        onSfxToggle: (Boolean) -> Unit,
        onDismiss: () -> Unit
) {
    Box(
            modifier =
                    Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                            ) { onDismiss() },
            contentAlignment = Alignment.Center
    ) {
        Column(
                modifier =
                        Modifier.fillMaxWidth(0.8f)
                                .background(
                                        Brush.verticalGradient(
                                                listOf(ButtonOrange, ButtonOrangeDark)
                                        ),
                                        RoundedCornerShape(16.dp)
                                )
                                .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                ) { /* Do nothing, swallow click */}
                                .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlineText(
                    text = "SETTINGS",
                    color = TextWhite,
                    outlineColor = TextStroke,
                    fontSize = 24.sp
            )

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                OutlineText(
                        text = "Music",
                        color = TextWhite,
                        outlineColor = TextStroke,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                )
                ToggleCircle(enabled = musicEnabled) { onMusicToggle(!musicEnabled) }
            }

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                OutlineText(
                        text = "Sounds",
                        color = TextWhite,
                        outlineColor = TextStroke,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                )
                ToggleCircle(enabled = sfxEnabled) { onSfxToggle(!sfxEnabled) }
            }
        }
    }
}

@Composable
private fun ToggleCircle(enabled: Boolean, onToggle: () -> Unit) {
    Box(
            modifier =
                    Modifier.size(32.dp)
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
                fontSize = 12.sp
        )
    }
}
