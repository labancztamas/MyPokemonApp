package com.labancztamas.mypokemonapp.network

import com.labancztamas.mypokemonapp.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AppHttpClient {
    val client = HttpClient {
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
        install(Logging) {
            logger = Logger.ANDROID
            level = if (BuildConfig.DEBUG) {
                LogLevel.BODY
            } else {
                LogLevel.NONE
            }
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}
