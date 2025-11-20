package com.koushik.kmpweatherapp.domain.repository

import com.koushik.kmpweatherapp.domain.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeather(): WeatherInfo
}