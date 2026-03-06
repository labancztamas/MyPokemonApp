package com.labancztamas.mypokemonapp.model.mappers

import com.labancztamas.mypokemonapp.model.PokemonDetails
import com.labancztamas.mypokemonapp.network.response.PokemonResponse

internal fun PokemonResponse.toPokemonDetails(isCaught: Boolean): PokemonDetails =
    PokemonDetails(
        notHiddenAbilities = abilities
            .filterNot { it.isHidden }
            .map { it.ability.name },
        height = height,
        name = name,
        imageUrl = image.other.home.imageUrl,
        weight = weight,
        type = types.first().type.name,
        isCaught = isCaught,
    )
