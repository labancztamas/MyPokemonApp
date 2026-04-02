package com.labancztamas.mypokemonapp.model.mappers

import com.labancztamas.mypokemonapp.model.PokemonTypes
import com.labancztamas.mypokemonapp.network.response.TypeResponse

fun TypeResponse.toPokemonTypes(): PokemonTypes =
    PokemonTypes(
        types = types.map { it.name }
    )
