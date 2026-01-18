package com.labancztamas.mypokemonapp.ui.screen.profile

sealed interface ProfileScreenUiState {
    data object Initial : ProfileScreenUiState
    data class Loaded(val data: String) : ProfileScreenUiState
    data object Error : ProfileScreenUiState
}
