package com.koushik.kmpweatherapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform