package com.labancztamas.mypokemonapp.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object MainScreen : Screen

    @Serializable
    data class ProfileScreen(val pokemonName: String) : Screen
}
