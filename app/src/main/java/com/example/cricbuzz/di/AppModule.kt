package com.example.cricbuzz.di

import com.example.cricbuzz.data.local.WeatherDao
import com.example.cricbuzz.data.remote.WeatherApiService
import com.example.cricbuzz.data.repository.WeatherRepository
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
    fun provideWeatherRepository(
        apiService: WeatherApiService,
        weatherDao: WeatherDao
    ): WeatherRepository {
        return WeatherRepository(apiService, weatherDao)
    }
}
