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
            else -> MainScreenUiState.Loaded(
                inputValues = inputValues,
                pokemonList = pokemonList
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenUiState.Loading
    )

    init {
        viewModelScope.launch {
            initalizeScreen()
        }
    }

    override fun getTypes() {
        viewModelScope.launch {
            pokemonInteractor.getTypesList()
                .catch { isError.emit(true) }
                .collect {
                    types.emit(it)
                }
        }
    }

    override fun updatePokemonList() {
        viewModelScope.launch {
            pokemonInteractor.getPokemonListItems(
                name = nameText.value,
                type = selectedType.value,
                isCaught = isCaughtBoxSelected.value,
            )
                .catch { isError.emit(true) }
                .collect {
                    pokemonList.emit(it)
                }
        }
    }

    override fun toggleCaughtBox(enabled: Boolean) {
        isCaughtBoxSelected.value = enabled
        updatePokemonList()
    }

    override fun setName(name: String) {
        nameText.value = name
        updatePokemonList()
    }

    override fun setType(type: String) {
        selectedType.value = type
        updatePokemonList()
    }

    override fun catchPokemon(pokemon: PokemonListItem) {
        viewModelScope.launch {
            try {
                pokemonInteractor.catchPokemon(name = pokemon.name, type = pokemon.type)
            } catch (_: Exception) {
                isError.emit(true)
            }
        }
    }

    override fun releasePokemon(pokemon: PokemonListItem) {
        viewModelScope.launch {
            try {
                pokemonInteractor.releasePokemon(name = pokemon.name, type = pokemon.type)
            } catch (_: Exception) {
                isError.emit(true)
            }
        }
    }

    override fun navigateToProfile(pokemon: PokemonListItem) {
        viewModelScope.launch {
            navigationEmitter.navigateTo(Screen.ProfileScreen(pokemon.name))
        }
    }

    override fun initalizeScreen() {
        getTypes()
        updatePokemonList()
    }
}
