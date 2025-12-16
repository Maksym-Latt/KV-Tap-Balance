package com.chicken.tapbalance.ui.skins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.tapbalance.data.ChickenSkinState
import com.chicken.tapbalance.model.ChickenSkin
import com.chicken.tapbalance.ui.components.AppButton
import com.chicken.tapbalance.ui.components.OutlineText
import com.chicken.tapbalance.ui.components.SkyBackground
import com.chicken.tapbalance.ui.theme.ButtonBlue
import com.chicken.tapbalance.ui.theme.ButtonBlueDark
import com.chicken.tapbalance.ui.theme.TextStroke
import com.chicken.tapbalance.ui.theme.TextWhite

@Composable
fun SkinsScreen(
    state: kotlinx.coroutines.flow.StateFlow<SkinsUiState>,
    onBack: () -> Unit,
    onPurchase: (ChickenSkin) -> Unit,
    onSelect: (ChickenSkin) -> Unit,
    onReady: () -> Unit
) {
    val uiState by state.collectAsState()
    LaunchedEffect(Unit) { onReady() }
    SkyBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                OutlineText(
                    text = "Points: ${uiState.points}",
                    color = TextWhite,
                    outlineColor = TextStroke,
                    fontSize = 18.sp
                )
            }
            SkinGrid(uiState.skins, onPurchase, onSelect)
        }
    }
}

@Composable
private fun SkinGrid(
    skins: List<ChickenSkinState>,
    onPurchase: (ChickenSkin) -> Unit,
    onSelect: (ChickenSkin) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        skins.chunked(2).forEach { pair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pair.forEach { state ->
                    SkinCard(state, onPurchase, onSelect, modifier = Modifier.weight(1f))
                }
                if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SkinCard(
    state: ChickenSkinState,
    onPurchase: (ChickenSkin) -> Unit,
    onSelect: (ChickenSkin) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(ButtonBlue, ButtonBlueDark)
                )
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlineText(
            text = state.skin.title,
            color = TextWhite,
            outlineColor = TextStroke,
            fontSize = 18.sp
        )
        Image(
            painter = painterResource(id = state.skin.calmRes),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )
        val buttonText = when {
            state.isSelected -> "Selected"
            state.isUnlocked -> "Select"
            else -> "Buy: ${state.skin.price}"
        }
        AppButton(text = buttonText, modifier = Modifier.fillMaxWidth()) {
            when {
                state.isSelected -> {}
                state.isUnlocked -> onSelect(state.skin)
                else -> onPurchase(state.skin)
            }
        }
    }
}
