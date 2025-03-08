package com.blinkitapp.data.model

data class Main(

val temp: Double,
val humidity: Int,
val pressure: Int,
    val icon:String

)

data class Wind(
    val speed: Double
)

data class Weather(
    val description: String
)

data class Sys(
    val sunrise: Long,
    val sunset: Long
)

