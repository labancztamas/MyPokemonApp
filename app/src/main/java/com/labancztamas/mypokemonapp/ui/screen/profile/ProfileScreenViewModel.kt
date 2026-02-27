package com.labancztamas.mypokemonapp.ui.screen.profile

import androidx.lifecycle.viewModelScope
import com.labancztamas.mypokemonapp.interactor.PokemonInteractor
import com.labancztamas.mypokemonapp.model.PokemonDetails
import com.labancztamas.mypokemonapp.navigation.NavigationEmitter
import com.labancztamas.mypokemonapp.ui.screen.profile.ProfileScreenContract.ProfileScreenAction
import com.labancztamas.mypokemonapp.utils.BaseViewModel
import com.labancztamas.mypokemonapp.utils.STATEFLOW_SUBSCRIPTION_TIME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class ProfileScreenViewModel(
    navigationEmitter: NavigationEmitter,
    private val pokemonInteractor: PokemonInteractor
) : ProfileScreenContract, BaseViewModel(
    navigationEmitter = navigationEmitter,
) {
    private val pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    private val isError = MutableStateFlow(false)
    private var pokemonName: String? = null

    override val uiState: StateFlow<ProfileScreenUiState> = combine(
        pokemonDetails,
        isError,
    ) { pokemonDetails, isError ->
        when {
            isError -> ProfileScreenUiState.Error
            pokemonDetails == null -> ProfileScreenUiState.Loading
            else -> ProfileScreenUiState.Content(pokemonDetails = pokemonDetails)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATEFLOW_SUBSCRIPTION_TIME),
        initialValue = ProfileScreenUiState.Initial
    )

    override fun onAction(action: ProfileScreenAction) {
        when (action) {
            ProfileScreenAction.CatchPokemon -> catchPokemon()
            ProfileScreenAction.ReleasePokemon -> releasePokemon()
            ProfileScreenAction.NavigateBack -> navigateBack()
            is ProfileScreenAction.InitializeScreen -> initializeScreen(action.name)
        }
    }

    private fun initializeScreen(name: String?) {
        isError.value = false
        pokemonName = name
        fetchPokemonDetails()
    }

    private fun fetchPokemonDetails() {
        val name = pokemonName
        if (!name.isNullOrEmpty()) {
            launch {
                pokemonInteractor.getPokemonDetails(name = name)
                    .catch {
                        Timber.e(it.toString())
                        isError.emit(true)
                    }
                    .collect {
                        pokemonDetails.emit(it)
                        Timber.d("Pokemon details: ${it.toString()}")
                    }
            }
        } else {
            Timber.e("Empty pokemon name!")
            isError.value = true
        }
    }

    private fun catchPokemon() {
        val pokemon = pokemonDetails.value
        if (pokemon != null) {
            launch {
                try {
                    pokemonInteractor.catchPokemon(
                        name = pokemon.name,
                        type = pokemon.type,
                    )
                    updateCaughtStateForPokemon()
                } catch (e: Exception) {
                    Timber.e(e.toString())
                    isError.emit(true)
                }
            }
        }
    }

    private fun releasePokemon() {
        val pokemon = pokemonDetails.value
        if (pokemon != null) {
            launch {
                try {
                    pokemonInteractor.releasePokemon(
                        name = pokemon.name,
                        type = pokemon.type
                    )
                    updateCaughtStateForPokemon()
                } catch (e: Exception) {
                    Timber.e(e.toString())
                    isError.emit(true)
                }
            }
        }
    }

    private suspend fun updateCaughtStateForPokemon() {
        val pokemon = pokemonDetails.value
        val caughtPokemonNames = pokemonInteractor.caughtPokemonsFlow.firstOrNull()
            ?: emptyList()
        var changed = false

        if (pokemon != null) {
            val updatedPokemon =
                if (caughtPokemonNames.contains(pokemon.name) && !pokemon.isCaught) {
                    changed = true
                    pokemon.copy(isCaught = true)
                } else if (!caughtPokemonNames.contains(pokemon.name) && pokemon.isCaught) {
                    changed = true
                    pokemon.copy(isCaught = false)
                } else {
                    pokemon
                }
            if (changed) {
                pokemonDetails.emit(updatedPokemon)
            }
        }
    }
}
