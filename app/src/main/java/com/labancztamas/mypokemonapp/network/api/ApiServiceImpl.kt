package com.labancztamas.mypokemonapp.network.api

import com.labancztamas.mypokemonapp.network.response.PokemonByTypeResponse
import com.labancztamas.mypokemonapp.network.response.PokemonResponse
import com.labancztamas.mypokemonapp.network.response.TypeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import timber.log.Timber

private const val BASE_URL = "https://pokeapi.co/api/v2/"
typealias HttpResponseCode = Int

class ApiServiceImpl(
    private val httpClient: HttpClient,
) : ApiService {

    override suspend fun getTypes(): TypeResponse =
        httpClient
            .get("${BASE_URL}type/")
            .body<TypeResponse>()


    override suspend fun getByName(name: String): Pair<PokemonResponse?, HttpResponseCode> {
        val response = httpClient
            .get("${BASE_URL}pokemon/${name}")
        return getBodyAndStatusCode<PokemonResponse>(response)
    }


    override suspend fun getByType(type: String): Pair<PokemonByTypeResponse?, HttpResponseCode> {
        val response = httpClient
            .get("${BASE_URL}type/${type}")
        return getBodyAndStatusCode<PokemonByTypeResponse>(response)
    }

    private suspend inline fun <reified T> getBodyAndStatusCode(
        response: HttpResponse
    ): Pair<T?, Int> {
        val body = try {
            response.body<T>()
        } catch (e: NoTransformationFoundException) {
            Timber.e(e)
            null
        }
        return body to response.status.value
    }
}
