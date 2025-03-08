package com.blinkitapp.di

import com.blinkitapp.data.network.ApiClient
import com.blinkitapp.data.network.WeatherApi
import com.blinkitapp.data.repository.WeatherRepositoryImpl
import com.blinkitapp.domain.repository.WeatherRepository
import com.blinkitapp.domain.usecase.GetWeatherUseCase
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
    fun provideWeatherApi(): WeatherApi = ApiClient.create()

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi): WeatherRepository = WeatherRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGetWeatherUseCase(repository: WeatherRepository): GetWeatherUseCase = GetWeatherUseCase(repository)


}
