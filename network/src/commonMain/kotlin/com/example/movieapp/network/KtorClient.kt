package com.example.movieapp.network

import com.example.movieapp.domain.util.Constants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(baseUrl: String, isTmdb: Boolean = false): HttpClient {
    return HttpClient {
        install(Logging) {
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        defaultRequest {
            url(baseUrl)
            if (isTmdb) {
                url.parameters.append("api_key", Constants.API_KEY)
                // Language parameter will be added in actual calls or we can use an expect/actual for locale
            }
        }
    }
}
