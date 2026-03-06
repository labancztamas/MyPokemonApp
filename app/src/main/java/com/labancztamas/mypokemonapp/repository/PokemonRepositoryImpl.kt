package com.labancztamas.mypokemonapp.repository

import com.labancztamas.mypokemonapp.db.PokemonDao
import com.labancztamas.mypokemonapp.db.PokemonEntity
import com.labancztamas.mypokemonapp.model.PokemonDetails
import com.labancztamas.mypokemonapp.model.PokemonListItem
import com.labancztamas.mypokemonapp.model.PokemonTypes
import com.labancztamas.mypokemonapp.model.mappers.toPokemonDetails
import com.labancztamas.mypokemonapp.model.mappers.toPokemonListItem
import com.labancztamas.mypokemonapp.model.mappers.toPokemonListItems
import com.labancztamas.mypokemonapp.model.mappers.toPokemonTypes
import com.labancztamas.mypokemonapp.network.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class PokemonRepositoryImpl(
    private val apiService: ApiService,
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override val caughtPokemonsFlow: Flow<List<String>> =
        pokemonDao.getCaughtPokemonNamesAsFlow()

    override suspend fun getTypesList(): Flow<PokemonTypes> =
        flow {
            val typeResponse = apiService.getTypes()
            emit(
                typeResponse.toPokemonTypes()
            )
        }

    override suspend fun getPokemonListItems(
        name: String?,
        type: String?,
        isCaught: Boolean?
    ): Flow<List<PokemonListItem>> =
        flow {
            val listItems: List<PokemonListItem> = when {
                isCaught == true && name.isNullOrEmpty() && type.isNullOrEmpty() ->
                    getAllCaughtPokemons()

                isCaught == true && name.isNullOrEmpty() && !type.isNullOrEmpty() ->
                    getCaughtPokemonsByType(type)

                isCaught == true && !name.isNullOrEmpty() && type.isNullOrEmpty() ->
                    getCaughtPokemonsByName(name)

                isCaught == true && !name.isNullOrEmpty() && !type.isNullOrEmpty() ->
                    getCaughtPokemonsByNameAndType(name, type)

                isCaught != true && name.isNullOrEmpty() && type.isNullOrEmpty() ->
                    emptyList()

                isCaught != true && !name.isNullOrEmpty() && type.isNullOrEmpty() -> {
                    getUncaughtPokemonByName(name).checkIfCaught()
                }

                isCaught != true && name.isNullOrEmpty() && !type.isNullOrEmpty() -> {
                    getUncaughtPokemonsByType(type).checkIfCaught()
                }

                isCaught != true && !name.isNullOrEmpty() && !type.isNullOrEmpty() -> {
                    getUncaughtPokemonsByNameAndType(name, type).checkIfCaught()
                }

                else -> emptyList()

            }
            emit(listItems)
        }

    override suspend fun getPokemonDetails(name: String): Flow<PokemonDetails> =
        flow {
            val pokemonResponse = apiService.getByName(name = name)
            val isCaught = pokemonDao.getCaughtPokemonByName(nameOfPokemon = name)
                .firstOrNull() != null

            emit(
                pokemonResponse.toPokemonDetails(isCaught = isCaught)
            )
        }


    override suspend fun catchPokemon(name: String, type: String) =
        pokemonDao.add(
            PokemonEntity(name = name, type = type)
        )


    override suspend fun releasePokemon(name: String, type: String) =
        pokemonDao.delete(
            PokemonEntity(name = name, type = type)
        )

    private suspend fun getUncaughtPokemonsByNameAndType(
        name: String,
        type: String
    ): List<PokemonListItem> {
        val pokemonResponse = apiService.getByName(name)

        val list: ArrayList<PokemonListItem> = arrayListOf()
        for (typeName in pokemonResponse.types) {
            if (typeName.type.name == type) {
                list.add(
                    pokemonResponse.toPokemonListItem(type = type)
                )
                break
            }
        }

        return list
    }

    private suspend fun getUncaughtPokemonsByType(type: String): List<PokemonListItem> {
        val response = apiService.getByType(type)
        return response.toPokemonListItems(type = type)
    }

    private suspend fun getUncaughtPokemonByName(name: String): List<PokemonListItem> {
        val response = apiService.getByName(name)
        return listOf(
            response.toPokemonListItem()
        )
    }

    private fun getCaughtPokemonsByNameAndType(
        name: String,
        type: String
    ): List<PokemonListItem> = pokemonDao.getCaughtPokemonByName(name)
        .filter { it.type == type }
        .map { pokemonEntity ->
            pokemonEntity.toPokemonListItem()
        }

    private fun getCaughtPokemonsByName(name: String): List<PokemonListItem> =
        pokemonDao.getCaughtPokemonByName(name).map { pokemonEntity ->
            pokemonEntity.toPokemonListItem()
        }

    private fun getCaughtPokemonsByType(type: String): List<PokemonListItem> =
        pokemonDao.getCaughtPokemonByType(type).map { pokemonEntity ->
            pokemonEntity.toPokemonListItem()
        }

    private fun getAllCaughtPokemons(): List<PokemonListItem> =
        pokemonDao.getCaughtPokemons().map { pokemonEntity ->
            pokemonEntity.toPokemonListItem()
        }

    private suspend fun List<PokemonListItem>.checkIfCaught(): List<PokemonListItem> {
        val caughtPokemonNames = caughtPokemonsFlow.firstOrNull() ?: emptyList()

        return this.map {
            if (caughtPokemonNames.contains(it.name) && !it.isCaught) {
                it.copy(isCaught = true)
            } else {
                it
            }
        }
    }
}
