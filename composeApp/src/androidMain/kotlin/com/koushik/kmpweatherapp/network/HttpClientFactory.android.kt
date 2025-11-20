package com.koushik.kmpweatherapp.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual object HttpClientProvider {
    actual val client: HttpClient = createDefaultClient(OkHttp)
}