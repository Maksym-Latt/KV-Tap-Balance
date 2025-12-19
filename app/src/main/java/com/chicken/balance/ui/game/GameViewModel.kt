package com.chicken.balance.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.balance.core.audio.AudioController
import com.chicken.balance.data.GameRepository
import com.chicken.balance.ui.game.GameConstants.CHICKEN_SWAY_STRENGTH
import com.chicken.balance.ui.game.GameConstants.CHICKEN_TILT_FOR_FALL
import com.chicken.balance.ui.game.GameConstants.TAP_STRENGTH
import com.chicken.balance.ui.game.GameConstants.WIND_STRENGTH_MAX
import com.chicken.balance.ui.game.GameConstants.WIND_STRENGTH_MIN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.abs
import kotlin.random.Random
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GameViewModel
@Inject
constructor(private val repository: GameRepository, private val audioController: AudioController) :
        ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var engineJob: Job? = null
    private var windJob: Job? = null
    private var leafSpawnJob: Job? = null

    private var velocity = 0f
    private var driftDirection = 1
    private var isWarmup = true
    private var warmupStartTime = 0L

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
                            points = points, // Total points from repo
                            currentSkin = skin,
                            musicEnabled = musicEnabled,
                            sfxEnabled = sfxEnabled
                    )
                }
                audioController.bindMusicState(musicEnabled)
                audioController.bindSfxState(sfxEnabled)
            }
                    .collect {}
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
                    windDirection = 0,
                    sessionPoints = 0,
                    lastSessionPoints = 0
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
                    windDirection = 0,
                    sessionPoints = 0,
                    lastSessionPoints = 0
            )
        }
        velocity = randomVelocity()
        driftDirection = if (Random.nextBoolean()) 1 else -1
        isWarmup = true
        warmupStartTime = System.currentTimeMillis()

        audioController.playGameMusic()
        engineJob = viewModelScope.launch { runGameLoop() }
        windJob?.cancel()
        windJob = viewModelScope.launch { runWindLoop() }
        leafSpawnJob?.cancel()
        leafSpawnJob = viewModelScope.launch { runLeafSpawner() }
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

    fun onLeftTap(x: Float, y: Float) {
        if (_uiState.value.isPaused || _uiState.value.isGameOver) return
        if (isWarmup) {
            isWarmup = false
        }
        velocity += TAP_STRENGTH
        addRipple(x, y)
        audioController.playTap()
    }

    fun onRightTap(x: Float, y: Float) {
        if (_uiState.value.isPaused || _uiState.value.isGameOver) return
        if (isWarmup) {
            isWarmup = false
        }
        velocity -= TAP_STRENGTH
        addRipple(x, y)
        audioController.playTap()
    }

    private fun addRipple(x: Float, y: Float) {
        val newRipple = RippleState(x = x, y = y)
        _uiState.update { it.copy(ripples = it.ripples + newRipple) }
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

        // Reset warmup timer when loop starts
        warmupStartTime = lastTime

        while (true) {
            delay(frameDelay)
            val now = System.currentTimeMillis()
            val deltaMs = now - lastTime
            val deltaSeconds = deltaMs / 1000f
            lastTime = now

            if (_uiState.value.isGameOver) break
            if (_uiState.value.isPaused) {
                if (isWarmup) {
                    warmupStartTime += deltaMs
                }
                continue
            }

            // Warmup / Stability Logic
            if (isWarmup) {
                if (now - warmupStartTime > 2000) {
                    isWarmup = false
                } else {
                    continue
                }
            }

            val windBoost =
                    if (_uiState.value.isWindActive)
                            _uiState.value.currentWindStrength * _uiState.value.windDirection
                    else 0f
            velocity += (CHICKEN_SWAY_STRENGTH * driftDirection + windBoost * deltaSeconds)
            velocity *= 0.995f

            val angleChange = velocity * deltaSeconds
            val newAngle = _uiState.value.chickenAngle + angleChange
            val newTime = _uiState.value.survivalTimeMs + deltaMs
            val updatedLeaves = updateLeaves()

            // Calculate session points: 1 point per 0.5s = 2 points per second
            // survivalTimeMs is in ms. 500ms = 1 point.
            val currentSessionPoints = (newTime / 500).toInt()

            if (abs(newAngle) >= CHICKEN_TILT_FOR_FALL) {
                handleGameOver(newTime, currentSessionPoints)
            } else {
                _uiState.update { state ->
                    state.copy(
                            chickenAngle = newAngle,
                            survivalTimeMs = newTime,
                            sessionPoints = currentSessionPoints,
                            leaves = updatedLeaves,
                            ripples = _uiState.value.ripples.filter { now - it.createdAt < 1000L }
                    )
                }
            }
        }
    }

    private suspend fun runWindLoop() {
        while (true) {
            delay(100)

            if (_uiState.value.isGameOver) break
            if (_uiState.value.isPaused) continue

            val waitTime = Random.nextLong(4000L, 9000L)
            var waited = 0L
            while (waited < waitTime) {
                delay(100)
                waited += 100
                if (_uiState.value.isGameOver) return
                if (_uiState.value.isPaused) {
                    continue
                }
            }
            if (_uiState.value.isGameOver) break

            _uiState.update { state ->
                val windStrength =
                        Random.nextFloat() * (WIND_STRENGTH_MAX - WIND_STRENGTH_MIN) +
                                WIND_STRENGTH_MIN
                state.copy(
                        isWindActive = true,
                        windDirection = if (Random.nextBoolean()) 1 else -1,
                        currentWindStrength = windStrength
                )
            }
            audioController.playWind()

            // Wind Duration
            val activeTime = Random.nextLong(2500L, 4000L)
            var activeWaited = 0L
            while (activeWaited < activeTime) {
                delay(100)
                if (_uiState.value.isGameOver) {
                    audioController.stopWind()
                    return
                }
                if (_uiState.value.isPaused) {
                    continue
                }
                activeWaited += 100
            }

            _uiState.update { state ->
                state.copy(isWindActive = false, windDirection = 0, currentWindStrength = 0f)
            }
            audioController.stopWind()
        }
    }

    private fun updateLeaves(): List<LeafState> {
        return _uiState.value.leaves.mapNotNull { leaf ->
            val newY = leaf.yFraction + leaf.speed
            val newRotation = (leaf.rotation + (leaf.speed * 360f)) % 360f

            val windX =
                    if (_uiState.value.isWindActive) {
                        (_uiState.value.currentWindStrength / 300f) * _uiState.value.windDirection
                    } else 0f
            val newX = leaf.xFraction + windX

            if (newY > 1.1f) {
                null
            } else {
                leaf.copy(yFraction = newY, xFraction = newX, rotation = newRotation)
            }
        }
    }

    private suspend fun runLeafSpawner() {
        while (true) {
            delay(Random.nextLong(3000L, 8000L))
            if (!_uiState.value.isPaused && !_uiState.value.isGameOver) {
                val newLeaves =
                        List(Random.nextInt(1, 4)) {
                            LeafState(
                                    xFraction = Random.nextFloat(),
                                    yFraction = -0.1f,
                                    speed = 0.002f + Random.nextFloat() * 0.004f,
                                    rotation = Random.nextFloat() * 360f
                            )
                        }
                _uiState.update { it.copy(leaves = it.leaves + newLeaves) }
            }
        }
    }

    private fun spawnLeaves(): List<LeafState> = emptyList()

    private suspend fun handleGameOver(time: Long, earnedPoints: Int) {
        _uiState.update {
            it.copy(
                    isGameOver = true,
                    isPaused = false,
                    survivalTimeMs = time,
                    points = it.points + earnedPoints,
                    sessionPoints = 0,
                    lastSessionPoints = earnedPoints
            )
        }
        audioController.playFall()
        repository.updateBestScore(time)
        repository.addPoints(earnedPoints)
        velocity = 0f
    }

    private fun randomVelocity(): Float = if (Random.nextBoolean()) 12f else -12f

    private fun stopEngine() {
        engineJob?.cancel()
        windJob?.cancel()
        leafSpawnJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        stopEngine()
    }
}
