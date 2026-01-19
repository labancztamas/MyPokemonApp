package com.labancztamas.mypokemonapp.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labancztamas.mypokemonapp.interactor.PokemonInteractor
import com.labancztamas.mypokemonapp.model.PokemonListItem
import com.labancztamas.mypokemonapp.model.PokemonTypes
import com.labancztamas.mypokemonapp.navigation.NavigationEmitter
import com.labancztamas.mypokemonapp.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class MainScreenViewModel(
    private val navigationEmitter: NavigationEmitter,
    private val pokemonInteractor: PokemonInteractor
) : ViewModel(), MainScreenContract {

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
            pokemonList == null -> MainScreenUiState.Loading
            else -> MainScreenUiState.Content(
                inputValues = inputValues,
                pokemonList = pokemonList
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenUiState.Initial
    )

    override fun onAction(action: MainScreenContract.MainScreenAction) {
        when (action) {
            is MainScreenContract.MainScreenAction.CatchPokemon -> catchPokemon(
                pokemon = action.pokemon
            )

            MainScreenContract.MainScreenAction.GetTypes -> getTypes()
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
        }
    }

    private fun getTypes() {
        viewModelScope.launch {
            pokemonInteractor.getTypesList()
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
        viewModelScope.launch {
            pokemonInteractor.getPokemonListItems(
                name = nameText.value,
                type = selectedType.value,
                isCaught = isCaughtBoxSelected.value,
            )
                .catch {
                    Timber.e(it.toString())
                    isError.emit(true)
                }
                .collect {
                    pokemonList.emit(it)
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
        viewModelScope.launch {
            try {
                pokemonInteractor.catchPokemon(name = pokemon.name, type = pokemon.type)
            } catch (e: Exception) {
                Timber.e(e.toString())
                isError.emit(true)
            }
        }
    }

    private fun releasePokemon(pokemon: PokemonListItem) {
        viewModelScope.launch {
            try {
                pokemonInteractor.releasePokemon(name = pokemon.name, type = pokemon.type)
            } catch (e: Exception) {
                Timber.e(e.toString())
                isError.emit(true)
            }
        }
    }

    private fun navigateToProfile(pokemon: PokemonListItem) {
        viewModelScope.launch {
            navigationEmitter.navigateTo(Screen.ProfileScreen(pokemon.name))
        }
    }

    private fun initializeScreen() {
        getTypes()
        pokemonList.value = emptyList()
    }
}
