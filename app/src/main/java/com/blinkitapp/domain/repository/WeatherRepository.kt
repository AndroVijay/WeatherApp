package com.blinkitapp.domain.repository

import com.blinkitapp.data.model.WeatherResponse

interface WeatherRepository {
    suspend fun getWeather(city: String): WeatherResponse
}