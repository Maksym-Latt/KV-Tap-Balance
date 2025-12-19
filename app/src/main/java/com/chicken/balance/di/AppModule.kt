package com.chicken.balance.di

import com.chicken.balance.data.GameRepository
import com.chicken.balance.data.PreferenceDataSource
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
}
