package com.chicken.tapbalance.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.tapbalance.core.audio.AudioController
import com.chicken.tapbalance.data.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GameRepository,
    private val audioController: AudioController
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var engineJob: Job? = null
    private var windJob: Job? = null

    private var velocity = 0f
    private var driftDirection = 1

    init {
        viewModelScope.launch {
            combine(
                repository.bestScore,
                repository.points,
                repository.selectedSkin,
                repository.musicEnabled,
                repository.sfxEnabled
            ) { best, points, skin, musicEnabled, sfxEnabled ->
                _uiState.update { state ->
                    state.copy(
                        bestScoreMs = best,
                        points = points,
                        currentSkin = skin,
                        musicEnabled = musicEnabled,
                        sfxEnabled = sfxEnabled
                    )
                }
                audioController.bindMusicState(musicEnabled)
                audioController.bindSfxState(sfxEnabled)
            }.collect { }
        }
        startIdle()
    }

    fun startIdle() {
        stopEngine()
        _uiState.update { state ->
            state.copy(
                chickenAngle = 0f,
                isGameOver = false,
                isPaused = false,
                survivalTimeMs = 0L,
                leaves = spawnLeaves(),
                isWindActive = false,
                windDirection = 0
            )
        }
        audioController.playMenuMusic()
    }

    fun startGame() {
        stopEngine()
        _uiState.update { state ->
            state.copy(
                chickenAngle = 0f,
                isPaused = false,
                isGameOver = false,
                survivalTimeMs = 0L,
                leaves = spawnLeaves(),
                isWindActive = false,
                windDirection = 0
            )
        }
        velocity = randomVelocity()
        driftDirection = if (Random.nextBoolean()) 1 else -1
        audioController.playGameMusic()
        engineJob = viewModelScope.launch { runGameLoop() }
        windJob?.cancel()
        windJob = viewModelScope.launch { runWindLoop() }
    }

    fun pauseGame() {
        _uiState.update { it.copy(isPaused = true) }
    }

    fun resumeGame() {
        _uiState.update { it.copy(isPaused = false) }
    }

    fun restartGame() {
        startGame()
    }

    fun onLeftTap() {
        velocity += 2.2f
        audioController.playTap()
    }

    fun onRightTap() {
        velocity -= 2.2f
        audioController.playTap()
    }

    fun onToggleMusic(enabled: Boolean) {
        viewModelScope.launch { repository.setMusicEnabled(enabled) }
    }

    fun onToggleSfx(enabled: Boolean) {
        viewModelScope.launch { repository.setSfxEnabled(enabled) }
    }

    private suspend fun runGameLoop() {
        val frameDelay = 16L
        var lastTime = System.currentTimeMillis()
        while (true) {
            delay(frameDelay)
            val now = System.currentTimeMillis()
            val deltaSeconds = (now - lastTime) / 1000f
            lastTime = now
            if (_uiState.value.isGameOver) break
            if (_uiState.value.isPaused) continue

            val windBoost = if (_uiState.value.isWindActive) 25f * _uiState.value.windDirection else 0f
            velocity += (0.45f * driftDirection + windBoost * deltaSeconds)
            velocity *= 0.995f

            val angleChange = velocity * deltaSeconds
            val newAngle = _uiState.value.chickenAngle + angleChange
            val newTime = _uiState.value.survivalTimeMs + (frameDelay)
            val updatedLeaves = updateLeaves()

            if (abs(newAngle) >= 55f) {
                handleGameOver(newTime)
            } else {
                _uiState.update { state ->
                    state.copy(
                        chickenAngle = newAngle,
                        survivalTimeMs = newTime,
                        leaves = updatedLeaves
                    )
                }
            }
        }
    }

    private suspend fun runWindLoop() {
        while (true) {
            delay(Random.nextLong(4000L, 9000L))
            _uiState.update { state ->
                state.copy(
                    isWindActive = true,
                    windDirection = if (Random.nextBoolean()) 1 else -1
                )
            }
            delay(Random.nextLong(2500L, 4000L))
            _uiState.update { state ->
                state.copy(isWindActive = false, windDirection = 0)
            }
        }
    }

    private fun updateLeaves(): List<LeafState> {
        val leaves = _uiState.value.leaves.ifEmpty { spawnLeaves() }
        return leaves.map { leaf ->
            val newY = leaf.yFraction + leaf.speed
            if (newY > 1f) {
                leaf.copy(xFraction = Random.nextFloat(), yFraction = 0f, speed = 0.002f + Random.nextFloat() * 0.004f)
            } else {
                leaf.copy(yFraction = newY)
            }
        }
    }

    private fun spawnLeaves(): List<LeafState> = List(3) {
        LeafState(
            xFraction = Random.nextFloat(),
            yFraction = Random.nextFloat() * 0.3f,
            speed = 0.002f + Random.nextFloat() * 0.004f
        )
    }

    private suspend fun handleGameOver(time: Long) {
        _uiState.update { it.copy(isGameOver = true, isPaused = false, survivalTimeMs = time) }
        audioController.playFall()
        repository.updateBestScore(time)
        repository.addPoints((time / 500).toInt())
        velocity = 0f
    }

    private fun randomVelocity(): Float = if (Random.nextBoolean()) 12f else -12f

    private fun stopEngine() {
        engineJob?.cancel()
        windJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        stopEngine()
    }
}
