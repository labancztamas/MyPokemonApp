package com.labancztamas.mypokemonapp.network.api

import com.labancztamas.mypokemonapp.network.response.PokemonByTypeResponse
import com.labancztamas.mypokemonapp.network.response.PokemonResponse
import com.labancztamas.mypokemonapp.network.response.TypeResponse

interface ApiService {

    suspend fun getTypes(): TypeResponse
    suspend fun getByName(name: String): PokemonResponse
    suspend fun getByType(type: String): PokemonByTypeResponse
}