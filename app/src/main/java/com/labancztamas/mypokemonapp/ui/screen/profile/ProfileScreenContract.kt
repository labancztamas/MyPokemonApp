package com.labancztamas.mypokemonapp.ui.screen.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileScreenContract {

    val uiState: StateFlow<ProfileScreenUiState>

    fun onAction(action: ProfileScreenAction)

    sealed interface ProfileScreenAction {
        data object CatchPokemon : ProfileScreenAction
        data object ReleasePokemon : ProfileScreenAction
        data object NavigateBack : ProfileScreenAction
        data class InitializeScreen(val name: String?) : ProfileScreenAction
    }
}
