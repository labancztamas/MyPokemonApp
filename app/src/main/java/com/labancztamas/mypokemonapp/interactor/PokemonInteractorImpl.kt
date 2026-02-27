package com.labancztamas.mypokemonapp.interactor

import com.labancztamas.mypokemonapp.db.PokemonDao
import com.labancztamas.mypokemonapp.db.PokemonEntity
import com.labancztamas.mypokemonapp.model.PokemonDetails
import com.labancztamas.mypokemonapp.model.PokemonListItem
import com.labancztamas.mypokemonapp.model.PokemonTypes
import com.labancztamas.mypokemonapp.network.api.ApiService
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class PokemonInteractorImpl(
    private val apiService: ApiService,
    private val pokemonDao: PokemonDao
) : PokemonInteractor {

    override val caughtPokemonsFlow: Flow<List<String>> =
        pokemonDao.getCaughtPokemonNamesAsFlow()

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
                    getAllCaughtPokemonsFromDB()

                isCaught == true && name.isNullOrEmpty() && !type.isNullOrEmpty() ->
                    getCaughtPokemonsByTypeFromDB(type)

                isCaught == true && !name.isNullOrEmpty() && type.isNullOrEmpty() ->
                    getCaughtPokemonsByNameFromDB(name)

                isCaught == true && !name.isNullOrEmpty() && !type.isNullOrEmpty() ->
                    getCaughtPokemonsByNameAndTypeFromDB(name, type)

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

    override suspend fun getPokemonDetails(name: String): Flow<PokemonDetails?> =
        flow {
            val response = apiService.getByName(name = name).first
            val isCaught = pokemonDao.getCaughtPokemonByName(nameOfPokemon = name)
                .firstOrNull() != null

            emit(
                response?.let {
                    PokemonDetails(
                        notHiddenAbilities = response.abilities
                            .filterNot { it.isHidden }
                            .map { it.ability.name },
                        height = response.height,
                        name = response.name,
                        imageUrl = response.image.other.home.imageUrl,
                        weight = response.weight,
                        type = response.types.first().type.name,
                        isCaught = isCaught,
                    )
                }
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
        val response = apiService.getByName(name)
        return if (response.second == HttpStatusCode.NotFound.value) {
            emptyList()
        } else {
            var list: List<PokemonListItem>? = null
            for (typeName in response.first!!.types) {
                if (typeName.type.name == type) {
                    list = listOf(
                        PokemonListItem(
                            type = type,
                            name = response.first!!.name,
                            isCaught = false
                        )
                    )
                    break
                }
            }
            list ?: emptyList()
        }
    }

    private suspend fun getUncaughtPokemonsByType(type: String): List<PokemonListItem> {
        val response = apiService.getByType(type)
        return if (response.second == HttpStatusCode.NotFound.value) {
            emptyList()
        } else {
            response.first!!.pokemon
                .map {
                    PokemonListItem(
                        type = type,
                        name = it.pokemon.name,
                        isCaught = false
                    )
                }
        }
    }

    private suspend fun getUncaughtPokemonByName(name: String): List<PokemonListItem> {
        val response = apiService.getByName(name)
        return if (response.second == HttpStatusCode.NotFound.value) {
            emptyList()
        } else {
            listOf(
                PokemonListItem(
                    type = response.first!!.types.first().type.name,
                    name = response.first!!.name,
                    isCaught = false
                )
            )
        }
    }

    private fun getCaughtPokemonsByNameAndTypeFromDB(
        name: String,
        type: String
    ): List<PokemonListItem> = pokemonDao.getCaughtPokemonByName(name)
        .filter { it.type == type }
        .map {
            PokemonListItem(
                type = it.type,
                name = it.name,
                isCaught = true
            )
        }

    private fun getCaughtPokemonsByNameFromDB(name: String): List<PokemonListItem> =
        pokemonDao.getCaughtPokemonByName(name).map {
            PokemonListItem(
                type = it.type,
                name = it.name,
                isCaught = true
            )
        }

    private fun getCaughtPokemonsByTypeFromDB(type: String): List<PokemonListItem> =
        pokemonDao.getCaughtPokemonByType(type).map {
            PokemonListItem(
                type = it.type,
                name = it.name,
                isCaught = true
            )
        }

    private fun getAllCaughtPokemonsFromDB(): List<PokemonListItem> =
        pokemonDao.getCaughtPokemons().map {
            PokemonListItem(
                type = it.type,
                name = it.name,
                isCaught = true
            )
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
