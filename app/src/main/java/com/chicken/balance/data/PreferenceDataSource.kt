package com.chicken.balance.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "chicken_prefs")

@Singleton
class PreferenceDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val bestScore = longPreferencesKey("best_score")
        val points = intPreferencesKey("points")
        val selectedSkin = stringPreferencesKey("selected_skin")
        val unlockedSkins = stringPreferencesKey("unlocked_skins")
        val musicEnabled = booleanPreferencesKey("music_enabled")
        val sfxEnabled = booleanPreferencesKey("sfx_enabled")
    }

    val bestScoreFlow: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[Keys.bestScore] ?: 0L
    }

    val pointsFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[Keys.points] ?: 0
    }

    val selectedSkinFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.selectedSkin] ?: "WHITE"
    }

    val unlockedSkinsFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[Keys.unlockedSkins]?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: setOf("WHITE")
    }

    val musicEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.musicEnabled] ?: true
    }

    val sfxEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.sfxEnabled] ?: true
    }

    suspend fun updateBestScore(value: Long) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.bestScore] ?: 0L
            if (value > current) {
                prefs[Keys.bestScore] = value
            }
        }
    }

    suspend fun addPoints(amount: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.points] ?: 0
            prefs[Keys.points] = (current + amount).coerceAtLeast(0)
        }
    }

    suspend fun spendPoints(amount: Int): Boolean {
        var success = false
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.points] ?: 0
            if (current >= amount) {
                prefs[Keys.points] = current - amount
                success = true
            }
        }
        return success
    }

    suspend fun setSelectedSkin(id: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.selectedSkin] = id
        }
    }

    suspend fun unlockSkin(id: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.unlockedSkins]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet() ?: mutableSetOf()
            current.add(id)
            prefs[Keys.unlockedSkins] = current.joinToString(",")
        }
    }

    suspend fun setMusicEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.musicEnabled] = enabled
        }
    }

    suspend fun setSfxEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.sfxEnabled] = enabled
        }
    }
}
