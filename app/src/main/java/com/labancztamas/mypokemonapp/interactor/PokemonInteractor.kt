package com.labancztamas.mypokemonapp.interactor

import com.labancztamas.mypokemonapp.model.PokemonDetails
import com.labancztamas.mypokemonapp.model.PokemonListItem
import com.labancztamas.mypokemonapp.model.PokemonTypes
import kotlinx.coroutines.flow.Flow

interface PokemonInteractor {

    suspend fun getTypesList(): Flow<PokemonTypes>
    suspend fun getPokemonListItems(
        name: String? = null,
        type: String? = null,
        isCaught: Boolean? = null
    ): Flow<List<PokemonListItem>>

    suspend fun getPokemonDetails(name: String): Flow<PokemonDetails?>
    suspend fun catchPokemon(name: String, type: String)
    suspend fun releasePokemon(name: String, type: String)
}
