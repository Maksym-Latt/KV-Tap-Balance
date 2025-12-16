package com.chicken.tapbalance.ui.skins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.tapbalance.core.audio.AudioController
import com.chicken.tapbalance.data.GameRepository
import com.chicken.tapbalance.data.ChickenSkinState
import com.chicken.tapbalance.model.ChickenSkin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkinsViewModel @Inject constructor(
    private val repository: GameRepository,
    private val audioController: AudioController
) : ViewModel() {
    private val _uiState = MutableStateFlow(SkinsUiState())
    val uiState: StateFlow<SkinsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.points,
                repository.skinsWithUnlockState
            ) { points, skins ->
                SkinsUiState(points = points, skins = skins)
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onSelectSkin(skin: ChickenSkin) {
        viewModelScope.launch { repository.selectSkin(skin) }
    }

    fun onPurchaseSkin(skin: ChickenSkin) {
        viewModelScope.launch {
            if (repository.tryPurchaseSkin(skin)) {
                repository.selectSkin(skin)
            }
        }
    }

    fun onScreenShown() {
        audioController.playMenuMusic()
    }
}

data class SkinsUiState(
    val points: Int = 0,
    val skins: List<ChickenSkinState> = emptyList()
)
