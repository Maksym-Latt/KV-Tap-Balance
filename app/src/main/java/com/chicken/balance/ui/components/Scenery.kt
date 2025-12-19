package com.chicken.balance.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.chicken.balance.R
import com.chicken.balance.ui.theme.AppGradients

@Composable
fun SkyBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                AppGradients.sky
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
    Image(
        painter = painterResource(id = R.drawable.fence),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        alignment = Alignment.BottomCenter
    )
}
