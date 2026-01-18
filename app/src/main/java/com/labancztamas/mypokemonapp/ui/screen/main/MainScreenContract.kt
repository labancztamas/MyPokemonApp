package com.labancztamas.mypokemonapp.ui.screen.main

import com.labancztamas.mypokemonapp.model.PokemonListItem
import kotlinx.coroutines.flow.StateFlow

interface MainScreenContract {

    val uiState: StateFlow<MainScreenUiState>

    fun getTypes()
    fun updatePokemonList()
    fun toggleCaughtBox(enabled: Boolean)
    fun setName(name: String)
    fun setType(type: String)
    fun catchPokemon(pokemon: PokemonListItem)
    fun releasePokemon(pokemon: PokemonListItem)
    fun navigateToProfile(pokemon: PokemonListItem)
    fun initalizeScreen()
}