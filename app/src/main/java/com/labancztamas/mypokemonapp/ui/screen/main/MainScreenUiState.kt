package com.labancztamas.mypokemonapp.ui.screen.main

import com.labancztamas.mypokemonapp.model.PokemonListItem
import com.labancztamas.mypokemonapp.model.PokemonTypes

sealed interface MainScreenUiState {
    data object Loading : MainScreenUiState
    data class Loaded(
        val inputValues: MainScreenInputValues,
        val pokemonList: List<PokemonListItem>
    ) : MainScreenUiState

    data object Error : MainScreenUiState
}

data class MainScreenInputValues(
    val nameText: String?,
    val selectableTypes: PokemonTypes?,
    val selectedType: String?,
    val isCaughtCheckBoxSelected: Boolean,
)
