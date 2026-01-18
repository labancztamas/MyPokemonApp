package com.labancztamas.mypokemonapp.network.response

import kotlinx.serialization.Serializable

@Serializable
data class PokemonByTypeResponse(
    val name: String,
    val pokemon: List<Pokemon>
)

@Serializable
data class Pokemon(
    val pokemon: PokemonX
)

@Serializable
data class PokemonX(
    val name: String
)
