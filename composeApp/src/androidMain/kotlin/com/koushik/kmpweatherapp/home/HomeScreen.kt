package com.koushik.kmpweatherapp.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.koushik.kmpweatherapp.domain.model.WeatherInfo
import com.koushik.kmpweatherapp.presentation.home.HomeUiState
import com.koushik.kmpweatherapp.presentation.home.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
) {
    val state = viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val s = state.value) {
            is HomeUiState.Loading -> {
                CircularProgressIndicator()
            }

            is HomeUiState.Success -> {
                WeatherContent(
                    weatherInfo = s.data,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            is HomeUiState.Empty -> {
                EmptyState(
                    message = "No data available",
                    onRetry = { viewModel.loadWeather() }
                )
            }

            is HomeUiState.Error -> {
                EmptyState(
                    message = s.message,
                    onRetry = { viewModel.loadWeather() }
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(weatherInfo: WeatherInfo, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Temperature: ${weatherInfo.temperature}°C", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Weather Code: ${weatherInfo.weatherCode}", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Humidity: ${weatherInfo.humidity}", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
private fun EmptyState(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}