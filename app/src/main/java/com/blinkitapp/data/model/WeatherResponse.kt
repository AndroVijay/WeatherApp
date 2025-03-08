package com.blinkitapp.data.model

data class WeatherResponse(

    val name: String,
    val main: Main,
    val wind: Wind,
    val weather: List<Weather>,
    val sys: Sys
)
