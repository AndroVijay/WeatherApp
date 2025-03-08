package com.blinkitapp.data.repository

import com.blinkitapp.data.model.WeatherResponse
import com.blinkitapp.data.network.WeatherApi
import com.blinkitapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi) : WeatherRepository {
    override suspend fun getWeather(city: String): WeatherResponse = api.getWeather(city, "45fc87fdbf208b0a8a125e5a61d0fc93")
}
