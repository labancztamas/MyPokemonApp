package com.labancztamas.mypokemonapp.network.api

import com.labancztamas.mypokemonapp.network.response.PokemonByTypeResponse
import com.labancztamas.mypokemonapp.network.response.PokemonResponse
import com.labancztamas.mypokemonapp.network.response.TypeResponse
import com.labancztamas.mypokemonapp.utils.AppException
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import timber.log.Timber

private const val BASE_URL = "https://pokeapi.co/api/v2/"

class ApiServiceImpl(
    private val httpClient: HttpClient,
) : ApiService {

    override suspend fun getTypes(): TypeResponse {
        val response = httpClient
            .get("${BASE_URL}type/")
        return getBodyOrError<TypeResponse>(response)
    }

    override suspend fun getByName(name: String): PokemonResponse {
        val response = httpClient
            .get("${BASE_URL}pokemon/${name}")
        return getBodyOrError<PokemonResponse>(response)
    }


    override suspend fun getByType(type: String): PokemonByTypeResponse {
        val response = httpClient
            .get("${BASE_URL}type/${type}")
        return getBodyOrError<PokemonByTypeResponse>(response)
    }

    private suspend inline fun <reified T> getBodyOrError(
        response: HttpResponse
    ): T {
        if (response.status.value == HttpStatusCode.NotFound.value) {
            throw AppException.NotFoundException()
        }

        if (response.status.value != HttpStatusCode.OK.value) {
            throw AppException.GeneralException()
        }

        val body = try {
            response.body<T>()
        } catch (e: NoTransformationFoundException) {
            Timber.e(e)
            throw AppException.InvalidResponseException()
        }

        return body
    }
}
