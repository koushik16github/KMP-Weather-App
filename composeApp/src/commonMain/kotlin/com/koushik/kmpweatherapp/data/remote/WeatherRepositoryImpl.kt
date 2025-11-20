package com.koushik.kmpweatherapp.data.remote

import com.koushik.kmpweatherapp.domain.model.WeatherInfo
import com.koushik.kmpweatherapp.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun getWeather(): WeatherInfo {
        val response = api.getCurrentWeather().current
        return WeatherInfo(
            temperature = response?.temperature,
            weatherCode = response?.weatherCode,
            humidity = response?.humidity
        )
    }
}