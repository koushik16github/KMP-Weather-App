package com.koushik.kmpweatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koushik.kmpweatherapp.data.remote.WeatherApi
import com.koushik.kmpweatherapp.data.remote.WeatherRepositoryImpl
import com.koushik.kmpweatherapp.domain.usecase.GetWeatherUseCase
import com.koushik.kmpweatherapp.home.HomeScreen
import com.koushik.kmpweatherapp.login.LoginScreen
import com.koushik.kmpweatherapp.presentation.home.HomeViewModel
import com.koushik.kmpweatherapp.presentation.login.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = "login") {
                composable("login") {
                    val loginViewModel = LoginViewModel()

                    LoginScreen(loginViewModel) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                composable("home") {
                    val weatherApi = WeatherApi()
                    val weatherRepositoryImpl = WeatherRepositoryImpl(weatherApi)
                    val getWeatherUseCase = GetWeatherUseCase(weatherRepositoryImpl)
                    val homeViewModel = HomeViewModel(getWeatherUseCase)

                    HomeScreen(homeViewModel)
                }
            }
        }
    }
}