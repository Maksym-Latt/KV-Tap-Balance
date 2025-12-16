package com.chicken.tapbalance.data

import com.chicken.tapbalance.model.ChickenSkin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val preferences: PreferenceDataSource
) {
    val bestScore: Flow<Long> = preferences.bestScoreFlow
    val points: Flow<Int> = preferences.pointsFlow
    val selectedSkin: Flow<ChickenSkin> = preferences.selectedSkinFlow.map { id ->
        ChickenSkin.values().firstOrNull { it.id == id } ?: ChickenSkin.WhiteHen
    }
    val unlockedSkins: Flow<Set<String>> = preferences.unlockedSkinsFlow
    val musicEnabled: Flow<Boolean> = preferences.musicEnabledFlow
    val sfxEnabled: Flow<Boolean> = preferences.sfxEnabledFlow

    val skinsWithUnlockState: Flow<List<ChickenSkinState>> = combine(
        unlockedSkins,
        selectedSkin
    ) { unlocked, selected ->
        ChickenSkin.entries.map { skin ->
            ChickenSkinState(
                skin = skin,
                isUnlocked = unlocked.contains(skin.id),
                isSelected = selected == skin
            )
        }
    }

    suspend fun updateBestScore(timeMs: Long) {
        preferences.updateBestScore(timeMs)
    }

    suspend fun addPoints(amount: Int) {
        preferences.addPoints(amount)
    }

    suspend fun tryPurchaseSkin(skin: ChickenSkin): Boolean {
        val success = preferences.spendPoints(skin.price)
        if (success) {
            preferences.unlockSkin(skin.id)
        }
        return success
    }

    suspend fun selectSkin(skin: ChickenSkin) {
        preferences.setSelectedSkin(skin.id)
    }

    suspend fun setMusicEnabled(enabled: Boolean) {
        preferences.setMusicEnabled(enabled)
    }

    suspend fun setSfxEnabled(enabled: Boolean) {
        preferences.setSfxEnabled(enabled)
    }
}

data class ChickenSkinState(
    val skin: ChickenSkin,
    val isUnlocked: Boolean,
    val isSelected: Boolean
)
