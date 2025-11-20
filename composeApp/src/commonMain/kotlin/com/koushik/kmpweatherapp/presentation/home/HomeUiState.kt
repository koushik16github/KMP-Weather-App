package com.koushik.kmpweatherapp.presentation.home

import com.koushik.kmpweatherapp.domain.model.WeatherInfo

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: WeatherInfo) : HomeUiState()
    object Empty : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}