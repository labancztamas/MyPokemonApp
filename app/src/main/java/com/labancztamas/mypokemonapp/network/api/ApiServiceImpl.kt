package com.labancztamas.mypokemonapp.network.api

import com.labancztamas.mypokemonapp.network.response.PokemonByTypeResponse
import com.labancztamas.mypokemonapp.network.response.PokemonResponse
import com.labancztamas.mypokemonapp.network.response.TypeResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val BASE_URL = "https://pokeapi.co/api/v2/"

class ApiServiceImpl(
    private val httpClient: HttpClient,
) : ApiService {

    override suspend fun getTypes(): TypeResponse =
        httpClient
            .get("${BASE_URL}type/")
            .body<TypeResponse>()

    override suspend fun getByName(name: String): PokemonResponse =
        httpClient
            .get("${BASE_URL}pokemon/${name}")
            .body<PokemonResponse>()

    override suspend fun getByType(type: String): PokemonByTypeResponse =
        httpClient
            .get("${BASE_URL}type/${type}")
            .body<PokemonByTypeResponse>()
}
