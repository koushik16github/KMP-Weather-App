package com.koushik.kmpweatherapp.domain.usecase

import com.koushik.kmpweatherapp.domain.model.WeatherInfo
import com.koushik.kmpweatherapp.domain.repository.WeatherRepository

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(): WeatherInfo {
        return repository.getWeather()
    }
}