package com.labancztamas.mypokemonapp.ui.screen.main

import com.labancztamas.mypokemonapp.model.PokemonListItem
import kotlinx.coroutines.flow.StateFlow

interface MainScreenContract {

    val uiState: StateFlow<MainScreenUiState>

    fun onAction(action: MainScreenAction)

    sealed interface MainScreenAction {
        data object UpdatePokemonList : MainScreenAction
        data class ToggleCaughtBox(val enabled: Boolean) : MainScreenAction
        data class SetName(val name: String) : MainScreenAction
        data class SetType(val type: String) : MainScreenAction
        data class CatchPokemon(val pokemon: PokemonListItem) : MainScreenAction
        data class ReleasePokemon(val pokemon: PokemonListItem) : MainScreenAction
        data class NavigateToProfile(val pokemon: PokemonListItem) : MainScreenAction
        data object InitializeScreen : MainScreenAction
    }
}
