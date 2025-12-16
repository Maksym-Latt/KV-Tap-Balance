package com.chicken.tapbalance.di

import com.chicken.tapbalance.core.audio.AudioController
import com.chicken.tapbalance.data.GameRepository
import com.chicken.tapbalance.data.PreferenceDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGameRepository(preferences: PreferenceDataSource): GameRepository =
        GameRepository(preferences)

    @Provides
    @Singleton
    fun provideAudioController(controller: AudioController): AudioController = controller
}
