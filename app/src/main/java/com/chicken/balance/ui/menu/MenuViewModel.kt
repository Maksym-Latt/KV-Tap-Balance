package com.chicken.balance.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.balance.core.audio.AudioController
import com.chicken.balance.data.GameRepository
import com.chicken.balance.model.ChickenSkin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: GameRepository,
    private val audioController: AudioController
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.bestScore,
                repository.points,
                repository.selectedSkin,
                repository.musicEnabled,
                repository.sfxEnabled
            ) { best, points, skin, music, sfx ->
                MenuUiState(
                    bestScoreMs = best,
                    points = points,
                    selectedSkin = skin,
                    musicEnabled = music,
                    sfxEnabled = sfx
                )
            }.collect { state ->
                _uiState.value = state
                audioController.bindMusicState(state.musicEnabled)
                audioController.bindSfxState(state.sfxEnabled)
            }
        }
        audioController.playMenuMusic()
    }

    fun setMusicEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setMusicEnabled(enabled) }
    }

    fun setSfxEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setSfxEnabled(enabled) }
    }
}

data class MenuUiState(
    val bestScoreMs: Long = 0L,
    val points: Int = 0,
    val selectedSkin: ChickenSkin = ChickenSkin.WhiteHen,
    val musicEnabled: Boolean = true,
    val sfxEnabled: Boolean = true
)
