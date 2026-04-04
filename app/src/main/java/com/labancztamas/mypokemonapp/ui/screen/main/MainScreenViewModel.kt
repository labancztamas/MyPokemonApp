package com.labancztamas.mypokemonapp.ui.screen.main

import androidx.lifecycle.viewModelScope
import com.labancztamas.mypokemonapp.model.PokemonListItem
import com.labancztamas.mypokemonapp.model.PokemonTypes
import com.labancztamas.mypokemonapp.navigation.NavigationEmitter
import com.labancztamas.mypokemonapp.navigation.Screen
import com.labancztamas.mypokemonapp.repository.PokemonRepository
import com.labancztamas.mypokemonapp.utils.AppException
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

class MainScreenViewModel(
    navigationEmitter: NavigationEmitter,
    private val pokemonRepository: PokemonRepository
) : MainScreenContract, BaseViewModel(
    navigationEmitter = navigationEmitter,
) {

    private val types = MutableStateFlow<PokemonTypes?>(null)
    private val nameText = MutableStateFlow("")
    private val selectedType = MutableStateFlow("")
    private val isCaughtBoxSelected = MutableStateFlow(false)
    private val pokemonList = MutableStateFlow<List<PokemonListItem>?>(null)
    private val isError = MutableStateFlow(false)

    private val inputValues = combine(
        types,
        nameText,
        selectedType,
        isCaughtBoxSelected
    ) { types, nameText, selectedType, isCaughtBoxSelected ->
        MainScreenInputValues(
            nameText = nameText,
            selectableTypes = types,
            selectedType = selectedType,
            isCaughtCheckBoxSelected = isCaughtBoxSelected
        )
    }

    override val uiState: StateFlow<MainScreenUiState> = combine(
        inputValues,
        pokemonList,
        isError
    ) { inputValues, pokemonList, isError ->
        when {
            isError -> MainScreenUiState.Error
            pokemonList == null || inputValues.selectableTypes == null
                -> MainScreenUiState.Loading

            else -> MainScreenUiState.Content(
                inputValues = inputValues,
                pokemonList = pokemonList
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STATEFLOW_SUBSCRIPTION_TIME),
        initialValue = MainScreenUiState.Initial
    )

    override fun onAction(action: MainScreenContract.MainScreenAction) {
        when (action) {
            is MainScreenContract.MainScreenAction.CatchPokemon -> catchPokemon(
                pokemon = action.pokemon
            )

            MainScreenContract.MainScreenAction.InitializeScreen -> initializeScreen()
            is MainScreenContract.MainScreenAction.NavigateToProfile -> navigateToProfile(
                pokemon = action.pokemon
            )

            is MainScreenContract.MainScreenAction.ReleasePokemon -> releasePokemon(
                pokemon = action.pokemon
            )

            is MainScreenContract.MainScreenAction.SetName -> setName(
                name = action.name
            )

            is MainScreenContract.MainScreenAction.SetType -> setType(
                type = action.type
            )

            is MainScreenContract.MainScreenAction.ToggleCaughtBox -> toggleCaughtBox(
                enabled = action.enabled
            )

            MainScreenContract.MainScreenAction.UpdatePokemonList -> updatePokemonList()
            MainScreenContract.MainScreenAction.CheckCaughtPokemons -> checkCaughtPokemons()
            MainScreenContract.MainScreenAction.NavigateBack -> navigateBack()
        }
    }

    private fun getTypes() {
        launchInViewModelScope {
            pokemonRepository.getTypesList()
                .catch {
                    Timber.e(it.toString())
                    isError.emit(true)
                }
                .collect {
                    types.emit(it)
                }
        }
    }

    private fun updatePokemonList() {
        launchInViewModelScope {
            pokemonRepository.getPokemonListItems(
                name = nameText.value,
                type = selectedType.value,
                isCaught = isCaughtBoxSelected.value,
            ).catch { exception ->
                if (exception is AppException.NotFoundException) {
                    pokemonList.emit(emptyList())
                } else {
                    Timber.e(exception.toString())
                    isError.emit(true)
                }
            }.collect { pokemons ->
                val distinctPokemons = pokemons.distinctBy { it.name }
                pokemonList.emit(distinctPokemons)
            }
        }
    }

    private fun toggleCaughtBox(enabled: Boolean) {
        isCaughtBoxSelected.value = enabled
    }

    private fun setName(name: String) {
        nameText.value = name
    }

    private fun setType(type: String) {
        selectedType.value = type
    }

    private fun catchPokemon(pokemon: PokemonListItem) {
        launchInViewModelScope {
            // TODO these should return a Flow<Unit> and use Flow.catch for error handling
            try {
                pokemonRepository.catchPokemon(
                    name = pokemon.name,
                    type = pokemon.type
                )
                updateCaughtStateForPokemonList()
            } catch (e: Exception) {
                Timber.e(e.toString())
                isError.emit(true)
            }
        }
    }

    private fun releasePokemon(pokemon: PokemonListItem) {
        launchInViewModelScope {
            try {
                pokemonRepository.releasePokemon(
                    name = pokemon.name,
                    type = pokemon.type
                )
                updateCaughtStateForPokemonList()
            } catch (e: Exception) {
                Timber.e(e.toString())
                isError.emit(true)
            }
        }
    }

    private fun navigateToProfile(pokemon: PokemonListItem) {
        navigateTo(
            Screen.ProfileScreen(
                pokemonName = pokemon.name
            )
        )
    }

    private fun initializeScreen() {
        getTypes()
        pokemonList.value = emptyList()
        isError.value = false
    }

    private fun checkCaughtPokemons() {
        launchInViewModelScope {
            updateCaughtStateForPokemonList()
        }
    }

    private suspend fun updateCaughtStateForPokemonList() {
        val pokemonsList = pokemonList.value
        val caughtPokemonNames = pokemonRepository.caughtPokemonsFlow.firstOrNull()
            ?: emptyList()
        var changed = false

        if (!pokemonsList.isNullOrEmpty()) {
            val updatedList = pokemonsList.map {
                if (caughtPokemonNames.contains(it.name) && !it.isCaught) {
                    changed = true
                    it.copy(isCaught = true)
                } else if (!caughtPokemonNames.contains(it.name) && it.isCaught) {
                    changed = true
                    it.copy(isCaught = false)
                } else {
                    it
                }
            }
            if (changed) {
                pokemonList.emit(updatedList)
            }
        }
    }
}
