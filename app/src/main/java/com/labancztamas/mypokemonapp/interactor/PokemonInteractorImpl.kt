package com.labancztamas.mypokemonapp.interactor

import com.labancztamas.mypokemonapp.db.PokemonDao
import com.labancztamas.mypokemonapp.db.PokemonEntity
import com.labancztamas.mypokemonapp.model.PokemonDetails
import com.labancztamas.mypokemonapp.model.PokemonListItem
import com.labancztamas.mypokemonapp.model.PokemonTypes
import com.labancztamas.mypokemonapp.network.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokemonInteractorImpl(
    private val apiService: ApiService,
    private val pokemonDao: PokemonDao
) : PokemonInteractor {
    override suspend fun getTypesList(): Flow<PokemonTypes> =
        flow {
            val response = apiService.getTypes()
            emit(
                PokemonTypes(
                    types = response.types.map { it.name }
                )
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
                    pokemonDao.getCaughtPokemons().map {
                        PokemonListItem(
                            type = it.type,
                            name = it.name,
                            isCaught = true
                        )
                    }

                isCaught == true && name.isNullOrEmpty() && !type.isNullOrEmpty() ->
                    pokemonDao.getCaughtPokemonByType(type).map {
                        PokemonListItem(
                            type = it.type,
                            name = it.name,
                            isCaught = true
                        )
                    }

                isCaught == true && !name.isNullOrEmpty() && type.isNullOrEmpty() ->
                    pokemonDao.getCaughtPokemonByName(name).map {
                        PokemonListItem(
                            type = it.type,
                            name = it.name,
                            isCaught = true
                        )
                    }

                isCaught == true && !name.isNullOrEmpty() && !type.isNullOrEmpty() ->
                    pokemonDao.getCaughtPokemonByName(name)
                        .filter { it.type == type }
                        .map {
                            PokemonListItem(
                                type = it.type,
                                name = it.name,
                                isCaught = true
                            )
                        }

                isCaught != true && name.isNullOrEmpty() && type.isNullOrEmpty() ->
                    emptyList()

                isCaught != true && !name.isNullOrEmpty() && type.isNullOrEmpty() -> {
                    val response = apiService.getByName(name)
                    listOf(
                        PokemonListItem(
                            type = response.types.first().type.name,
                            name = response.name,
                            isCaught = false
                        )
                    )
                }

                isCaught != true && name.isNullOrEmpty() && !type.isNullOrEmpty() -> {
                    apiService.getByType(type).pokemon
                        .map {
                            PokemonListItem(
                                type = type,
                                name = it.pokemon.name,
                                isCaught = false
                            )
                        }
                }

                isCaught != true && !name.isNullOrEmpty() && !type.isNullOrEmpty() -> {
                    val response = apiService.getByName(name)
                    var list: List<PokemonListItem>? = null
                    for (typeName in response.types) {
                        if (typeName.type.name == type) {
                            list = listOf(
                                PokemonListItem(
                                    type = type,
                                    name = response.name,
                                    isCaught = false
                                )
                            )
                            break
                        }
                    }
                    list ?: emptyList()
                }

                else -> emptyList()

            }
            emit(listItems)
        }

    override suspend fun getPokemonDetails(name: String): Flow<PokemonDetails> =
        flow {
            val response = apiService.getByName(name = name)
            emit(
                PokemonDetails(
                    notHiddenAbilities = response.abilities
                        .filterNot { it.isHidden }
                        .map { it.ability.name },
                    height = response.height,
                    id = response.id,
                    name = response.name,
                    imageUrl = response.image.url,
                    weight = response.weight
                )
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
}
