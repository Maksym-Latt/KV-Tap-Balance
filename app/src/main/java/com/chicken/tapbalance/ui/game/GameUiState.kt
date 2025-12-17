package com.chicken.tapbalance.ui.game

import com.chicken.tapbalance.model.ChickenSkin

data class GameUiState(
    val chickenAngle: Float = 0f,
    val windDirection: Int = 0,
    val isWindActive: Boolean = false,
    val survivalTimeMs: Long = 0L,
    val bestScoreMs: Long = 0L,
    val points: Int = 0,
    val currentSkin: ChickenSkin = ChickenSkin.WhiteHen,
    val isPaused: Boolean = false,
    val isGameOver: Boolean = false,
    val leaves: List<LeafState> = emptyList(),
    val musicEnabled: Boolean = true,
    val sfxEnabled: Boolean = true
)

data class LeafState(
    val xFraction: Float,
    val yFraction: Float,
    val speed: Float,
    val rotation: Float
)
