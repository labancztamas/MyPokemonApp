package com.labancztamas.mypokemonapp.model.mappers

import com.labancztamas.mypokemonapp.db.PokemonEntity
import com.labancztamas.mypokemonapp.model.PokemonListItem
import com.labancztamas.mypokemonapp.network.response.PokemonByTypeResponse
import com.labancztamas.mypokemonapp.network.response.PokemonResponse

fun PokemonEntity.toPokemonListItem(isCaught: Boolean = true): PokemonListItem =
    PokemonListItem(
        type = type,
        name = name,
        isCaught = isCaught,
    )

fun PokemonResponse.toPokemonListItem(
    type: String? = null,
    isCaught: Boolean = false
): PokemonListItem =
    PokemonListItem(
        type = type ?: types.first().type.name,
        name = name,
        isCaught = isCaught,
    )

fun PokemonByTypeResponse.toPokemonListItems(
    type: String,
    isCaught: Boolean = false
): List<PokemonListItem> =
    pokemon
        .map {
            PokemonListItem(
                type = type,
                name = it.pokemon.name,
                isCaught = isCaught,
            )
        }
