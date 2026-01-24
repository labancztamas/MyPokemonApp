package com.labancztamas.mypokemonapp.ui.screen.profile

import com.labancztamas.mypokemonapp.model.PokemonDetails

sealed interface ProfileScreenUiState {
    data object Initial : ProfileScreenUiState
    data object Loading : ProfileScreenUiState
    data class Content(val pokemonDetails: PokemonDetails) : ProfileScreenUiState
    data object Error : ProfileScreenUiState
}
