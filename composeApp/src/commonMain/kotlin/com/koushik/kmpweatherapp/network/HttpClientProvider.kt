package com.koushik.kmpweatherapp.network

import io.ktor.client.HttpClient

expect object HttpClientProvider {
    val client: HttpClient
}