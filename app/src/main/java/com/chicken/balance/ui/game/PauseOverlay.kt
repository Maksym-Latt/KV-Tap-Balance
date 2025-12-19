package com.chicken.balance.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.balance.ui.components.AppButton
import com.chicken.balance.ui.components.AppIconButton
import com.chicken.balance.ui.components.OutlineText
import com.chicken.balance.ui.menu.SettingsPanelContainer
import com.chicken.balance.ui.menu.SettingsPanelSwitch

@Composable
fun PauseOverlay(
    musicEnabled: Boolean,
    sfxEnabled: Boolean,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit,
    onBack: () -> Unit = onResume
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.82f)
                .wrapContentHeight()
        ) {
            SettingsPanelContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlineText(
                        text = "Paused",
                        fontSize = 45.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlineText(
                            text = "Music",
                            fontSize = 26.sp,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        SettingsPanelSwitch(
                            checked = musicEnabled,
                            onCheckedChange = onMusicToggle
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlineText(
                            text = "Sounds",
                            fontSize = 26.sp,
                            modifier = Modifier,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        SettingsPanelSwitch(
                            checked = sfxEnabled,
                            onCheckedChange = onSfxToggle
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    AppButton(
                        text = "Restart",
                        modifier = Modifier.fillMaxWidth(0.86f)
                    ) { onRestart() }
                    Spacer(modifier = Modifier.height(18.dp))
                    AppButton(
                        text = "Menu",
                        modifier = Modifier.fillMaxWidth(0.86f)
                    ) { onMenu() }
                }
            }

            AppIconButton(
                painter = rememberVectorPainter(Icons.Default.ArrowBack),
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-14).dp, y = (-14).dp),
                size = 58.dp,
                iconSize = 30.dp,
                borderWidth = 3.dp,
                onClick = onResume
            )
        }
    }
}
