package com.koushik.kmpweatherapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("current") val current: CurrentWeather? = null,
)

@Serializable
data class CurrentWeather(
    @SerialName("temperature_2m") val temperature: Double? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
    @SerialName("relative_humidity_2m") val humidity: Int? = null
)