package com.koushik.kmpweatherapp.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createDefaultClient(engine: HttpClientEngineFactory<*>): HttpClient {
    val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    return HttpClient(engine) {
        install(ContentNegotiation) { json(jsonConfig) }
        install(HttpTimeout) {
            requestTimeoutMillis = 20_000
            connectTimeoutMillis = 20_000
            socketTimeoutMillis = 20_000
        }
    }
}
