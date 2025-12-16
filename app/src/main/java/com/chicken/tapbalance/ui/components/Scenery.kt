package com.chicken.tapbalance.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.tapbalance.R
import com.chicken.tapbalance.ui.theme.FenceShadow
import com.chicken.tapbalance.ui.theme.FenceWood
import com.chicken.tapbalance.ui.theme.SkyBottom
import com.chicken.tapbalance.ui.theme.SkyTop

@Composable
fun SkyBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(SkyTop, SkyBottom))
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.9f
        )
        content()
    }
}

@Composable
fun Fence(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(FenceWood)
                .padding(horizontal = 4.dp)
        )
        Box(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
                .offset(y = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(FenceShadow)
        )
    }
}
