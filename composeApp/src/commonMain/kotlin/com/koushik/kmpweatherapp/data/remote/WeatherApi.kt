package com.koushik.kmpweatherapp.data.remote

import com.koushik.kmpweatherapp.data.model.WeatherResponse
import com.koushik.kmpweatherapp.network.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get

class WeatherApi {

    private val client = HttpClientProvider.client

    suspend fun getCurrentWeather(): WeatherResponse {
        return client.get(
            "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m&current=temperature_2m,weather_code,relative_humidity_2m"
        ).body()
    }
}