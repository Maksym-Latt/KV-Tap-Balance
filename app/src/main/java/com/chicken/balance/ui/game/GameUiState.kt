package com.chicken.balance.ui.game

import com.chicken.balance.model.ChickenSkin

data class GameUiState(
        val chickenAngle: Float = 0f,
        val windDirection: Int = 0,
        val isWindActive: Boolean = false,
        val currentWindStrength: Float = 0f,
        val survivalTimeMs: Long = 0L,
        val bestScoreMs: Long = 0L,
        val points: Int = 0,
        val sessionPoints: Int = 0,
        val lastSessionPoints: Int = 0,
        val currentSkin: ChickenSkin = ChickenSkin.WhiteHen,
        val isPaused: Boolean = false,
        val isGameOver: Boolean = false,
        val leaves: List<LeafState> = emptyList(),
        val musicEnabled: Boolean = true,
        val sfxEnabled: Boolean = true,
        val ripples: List<RippleState> = emptyList()
)

data class LeafState(
        val xFraction: Float,
        val yFraction: Float,
        val speed: Float,
        val rotation: Float
)

data class RippleState(
        val id: Long = System.nanoTime(),
        val x: Float,
        val y: Float,
        val createdAt: Long = System.currentTimeMillis()
)
