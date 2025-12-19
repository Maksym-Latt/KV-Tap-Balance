package com.chicken.balance.ui.menu

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.balance.ui.components.AppIconButton
import com.chicken.balance.ui.components.OutlineText

@Composable
fun SettingsOverlay(
    musicEnabled: Boolean,
    sfxEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit,
    onDismiss: () -> Unit
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
                        text = "Settings",
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.height(22.dp))

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

                    Spacer(modifier = Modifier.height(8.dp))
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
                onClick = onDismiss
            )
        }
    }
}

@Composable
public fun SettingsPanelContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(28.dp)
    val panelBrush = remember {
        Brush.verticalGradient(
            listOf(
                Color(0xFF7EF3EA),
                Color(0xFF49C8C0),
                Color(0xFF2FA29B)
            )
        )
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(panelBrush)
            .border(4.dp, Color.Black, shape),
        content = content
    )
}

@Composable
fun SettingsPanelSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(999.dp)

    val trackBrush = remember {
        Brush.verticalGradient(
            listOf(
                Color(0xFF6BB8FF),
                Color(0xFF2E6FCD)
            )
        )
    }

    val knobBrush = remember {
        Brush.verticalGradient(
            listOf(
                Color(0xFF7EF3EA),
                Color(0xFF49C8C0)
            )
        )
    }

    Box(
        modifier = modifier
            .size(width = 86.dp, height = 40.dp)
            .clip(shape)
            .background(trackBrush)
            .border(1.dp, Color.Black, shape)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        val x by animateDpAsState(
            targetValue = if (checked) 46.dp else 6.dp,
            label = "knobX"
        )

        Box(
            modifier = Modifier
                .offset(x = x)
                .size(28.dp)
                .clip(CircleShape)
                .background(knobBrush)
                .border(2.dp,    Color(0xff66e3d9), CircleShape)
        )
    }
}

