package com.labancztamas.mypokemonapp.ui.screen.profile

import androidx.lifecycle.ViewModel
import com.labancztamas.mypokemonapp.interactor.PokemonInteractor
import com.labancztamas.mypokemonapp.navigation.NavigationEmitter
import kotlinx.coroutines.flow.StateFlow

class ProfileScreenViewModel(
    private val navigationEmitter: NavigationEmitter,
    private val pokemonInteractor: PokemonInteractor
) : ViewModel(), ProfileScreenContract {
    override val uiState: StateFlow<ProfileScreenUiState>
        get() = TODO("Not yet implemented")

    override fun fetchPokemonDetails() {
        TODO("Not yet implemented")
    }

    override fun catchPokemon() {
        TODO("Not yet implemented")
    }

    override fun releasePokemon() {
        TODO("Not yet implemented")
    }


}