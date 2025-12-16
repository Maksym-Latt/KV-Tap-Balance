package com.chicken.tapbalance.core.audio

import android.content.Context
import android.media.MediaPlayer
import com.chicken.tapbalance.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var menuPlayer: MediaPlayer? = null
    private var gamePlayer: MediaPlayer? = null
    private var tapPlayer: MediaPlayer? = null
    private var fallPlayer: MediaPlayer? = null

    var isMusicEnabled: Boolean = true
        private set
    var isSfxEnabled: Boolean = true
        private set

    fun bindMusicState(musicEnabled: Boolean) {
        isMusicEnabled = musicEnabled
        if (!musicEnabled) {
            menuPlayer?.pause()
            gamePlayer?.pause()
        }
    }

    fun bindSfxState(sfxEnabled: Boolean) {
        isSfxEnabled = sfxEnabled
    }

    fun playMenuMusic() {
        if (!isMusicEnabled) return
        if (menuPlayer == null) {
            menuPlayer = MediaPlayer.create(context, R.raw.menu_music).apply {
                isLooping = true
            }
        }
        gamePlayer?.pause()
        menuPlayer?.start()
    }

    fun playGameMusic() {
        if (!isMusicEnabled) return
        if (gamePlayer == null) {
            gamePlayer = MediaPlayer.create(context, R.raw.game_music).apply {
                isLooping = true
            }
        }
        menuPlayer?.pause()
        gamePlayer?.start()
    }

    fun playTap() {
        if (!isSfxEnabled) return
        tapPlayer?.release()
        tapPlayer = MediaPlayer.create(context, R.raw.sfx_tap)
        tapPlayer?.start()
    }

    fun playFall() {
        if (!isSfxEnabled) return
        fallPlayer?.release()
        fallPlayer = MediaPlayer.create(context, R.raw.sfx_fall)
        fallPlayer?.start()
    }

    fun onAppBackground() {
        menuPlayer?.pause()
        gamePlayer?.pause()
    }

    fun onAppForeground() {
        if (!isMusicEnabled) return
        if (gamePlayer?.isPlaying == false) gamePlayer?.start()
        if (menuPlayer?.isPlaying == false) menuPlayer?.start()
    }

    fun release() {
        menuPlayer?.release(); menuPlayer = null
        gamePlayer?.release(); gamePlayer = null
        tapPlayer?.release(); tapPlayer = null
        fallPlayer?.release(); fallPlayer = null
    }
}
