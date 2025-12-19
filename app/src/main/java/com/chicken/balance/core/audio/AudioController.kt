package com.chicken.balance.core.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.chicken.balance.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Singleton
class AudioController @Inject constructor(@ApplicationContext private val context: Context) {
    private var menuPlayer: MediaPlayer? = null
    private var gamePlayer: MediaPlayer? = null
    private var windPlayer: MediaPlayer? = null
    private var currentMusic: MediaPlayer? = null

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    // SoundPool for short SFX
    private val soundPool: SoundPool
    private val tapSoundId: Int
    private val fallSoundId: Int
    private var isTapLoaded = false
    private var isFallLoaded = false

    var isMusicEnabled: Boolean = false
        private set
    var isSfxEnabled: Boolean = false
        private set

    init {
        val audioAttributes =
                AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()

        soundPool = SoundPool.Builder().setMaxStreams(4).setAudioAttributes(audioAttributes).build()

        tapSoundId = soundPool.load(context, R.raw.sfx_tap, 1)
        fallSoundId = soundPool.load(context, R.raw.sfx_fall, 1)

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                if (sampleId == tapSoundId) isTapLoaded = true
                if (sampleId == fallSoundId) isFallLoaded = true
            }
        }
    }

    fun bindMusicState(musicEnabled: Boolean) {
        isMusicEnabled = musicEnabled
        if (!musicEnabled) {
            if (menuPlayer?.isPlaying == true) menuPlayer?.pause()
            if (gamePlayer?.isPlaying == true) gamePlayer?.pause()
        } else {
            // Resume current music if it was playing or should be playing
            if (currentMusic?.isPlaying == false) {
                currentMusic?.start()
            }
        }
    }

    fun bindSfxState(sfxEnabled: Boolean) {
        isSfxEnabled = sfxEnabled
        if (!sfxEnabled) {
            if (windPlayer?.isPlaying == true) windPlayer?.pause()
        }
    }

    fun playMenuMusic() {
        if (menuPlayer == null) {
            menuPlayer = MediaPlayer.create(context, R.raw.menu_music).apply { isLooping = true }
        }

        // Always update current intention
        currentMusic = menuPlayer

        if (isMusicEnabled) {
            if (gamePlayer?.isPlaying == true) {
                gamePlayer?.pause()
            }
            if (menuPlayer?.isPlaying == false) {
                menuPlayer?.start()
            }
        } else {
            // Ensure paused if disabled (safety)
            if (gamePlayer?.isPlaying == true) gamePlayer?.pause()
            if (menuPlayer?.isPlaying == true) menuPlayer?.pause()
        }
    }

    fun playGameMusic() {
        if (gamePlayer == null) {
            gamePlayer = MediaPlayer.create(context, R.raw.game_loop).apply { isLooping = true }
        }

        // Always update current intention
        currentMusic = gamePlayer

        if (isMusicEnabled) {
            if (menuPlayer?.isPlaying == true) {
                menuPlayer?.pause()
            }
            if (gamePlayer?.isPlaying == false) {
                gamePlayer?.start()
            }
        } else {
            // Ensure paused if disabled (safety)
            if (menuPlayer?.isPlaying == true) menuPlayer?.pause()
            if (gamePlayer?.isPlaying == true) gamePlayer?.pause()
        }
    }

    fun playTap() {
        if (!isSfxEnabled || !isTapLoaded) return
        soundPool.play(tapSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun playFall() {
        if (!isSfxEnabled || !isFallLoaded) return
        soundPool.play(fallSoundId, 1f, 1f, 1, 0, 1f)
    }

    fun playWind() {
        if (!isSfxEnabled) return
        if (windPlayer == null) {
            windPlayer = MediaPlayer.create(context, R.raw.sfx_wind)
        }
        windPlayer?.setVolume(1.0f, 1.0f)
        if (windPlayer?.isPlaying == false) {
            windPlayer?.start()
        }
    }

    fun stopWind() {
        scope.launch {
            val player = windPlayer ?: return@launch
            if (!player.isPlaying) return@launch

            var volume = 1.0f
            while (volume > 0.0f) {
                volume -= 0.1f
                if (volume < 0f) volume = 0f
                try {
                    player.setVolume(volume, volume)
                } catch (e: Exception) {
                    // Player might be released
                    break
                }
                delay(100) // 10 steps * 100ms = 1s fade out
            }
            try {
                if (player.isPlaying) {
                    player.pause()
                    player.seekTo(0)
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    fun onAppBackground() {
        if (menuPlayer?.isPlaying == true) menuPlayer?.pause()
        if (gamePlayer?.isPlaying == true) gamePlayer?.pause()
        if (windPlayer?.isPlaying == true) windPlayer?.pause()
    }

    fun onAppForeground() {
        if (isMusicEnabled) {
            currentMusic?.start()
        }
    }

    fun release() {
        menuPlayer?.release()
        menuPlayer = null
        gamePlayer?.release()
        gamePlayer = null
        windPlayer?.release()
        windPlayer = null
        soundPool.release()
    }
}
