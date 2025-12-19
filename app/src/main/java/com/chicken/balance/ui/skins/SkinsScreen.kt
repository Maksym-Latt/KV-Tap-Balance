package com.chicken.balance.ui.skins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.balance.data.ChickenSkinState
import com.chicken.balance.model.ChickenSkin
import com.chicken.balance.ui.components.AppIconButton
import com.chicken.balance.ui.components.Fence
import com.chicken.balance.ui.components.OutlineText
import com.chicken.balance.ui.components.PointsBadge
import com.chicken.balance.ui.components.SkyBackground
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SkinsScreen(
    state: StateFlow<SkinsUiState>,
    onBack: () -> Unit,
    onPurchase: (ChickenSkin) -> Unit,
    onSelect: (ChickenSkin) -> Unit,
    onReady: () -> Unit
) {
    val uiState by state.collectAsState()
    LaunchedEffect(Unit) { onReady() }

    SkyBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Fence(modifier = Modifier.align(Alignment.BottomCenter))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppIconButton(
                        painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                        contentDescription = "Back",
                        size = 58.dp,
                        iconSize = 30.dp,
                        onClick = onBack
                    )

                    PointsBadge(points = uiState.points)
                }

                SkinsGrid(
                    skins = uiState.skins,
                    onPurchase = onPurchase,
                    onSelect = onSelect,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SkinsGrid(
    skins: List<ChickenSkinState>,
    onPurchase: (ChickenSkin) -> Unit,
    onSelect: (ChickenSkin) -> Unit,
    modifier: Modifier = Modifier
) {
    val rows = skins.chunked(2)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        rows.forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                pair.forEach { item ->
                    SkinCell(
                        state = item,
                        onPurchase = onPurchase,
                        onSelect = onSelect,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SkinCell(
    state: ChickenSkinState,
    onPurchase: (ChickenSkin) -> Unit,
    onSelect: (ChickenSkin) -> Unit,
    modifier: Modifier = Modifier
) {
    val (line1, line2) = remember(state.skin.title) { splitTwoWords(state.skin.title) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlineText(text = line1, fontSize = 22.sp)
        OutlineText(text = line2, fontSize = 22.sp)

        Image(
            painter = painterResource(id = state.skin.calmRes),
            contentDescription = null,
            modifier = Modifier.size(170.dp),
            contentScale = ContentScale.Fit
        )

        val (text, enabled, onClick) = when {
            state.isSelected -> Triple("Selected", false) { }
            state.isUnlocked -> Triple("Select", true) { onSelect(state.skin) }
            else -> Triple("Buy: ${state.skin.price}", true) { onPurchase(state.skin) }
        }

        SkinActionButton(
            text = text,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(0.9f),
            onClick = onClick
        )
    }
}

@Composable
private fun SkinActionButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderWidth: Dp = 2.dp,
    borderColor: Color = Color.Black,
    background: Brush = Brush.verticalGradient(listOf(Color(0xff7cb6fc), Color(0xff2468be))),
    disabledBackground: Brush = Brush.verticalGradient(listOf(Color(0xFFB9B9B9), Color(0xFF7E7E7E))),
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(shape)
            .background(if (enabled) background else disabledBackground)
            .border(borderWidth, borderColor, shape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        OutlineText(
            text = text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

private fun splitTwoWords(title: String): Pair<String, String> {
    val parts = title.trim().split(Regex("\\s+"), limit = 2)
    return when (parts.size) {
        0 -> "" to ""
        1 -> parts[0] to ""
        else -> parts[0] to parts[1]
    }
}
