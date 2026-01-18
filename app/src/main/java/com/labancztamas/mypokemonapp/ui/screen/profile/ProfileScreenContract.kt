package com.labancztamas.mypokemonapp.ui.screen.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileScreenContract {

    val uiState: StateFlow<ProfileScreenUiState>

    fun fetchPokemonDetails()
    fun catchPokemon()
    fun releasePokemon()
}
