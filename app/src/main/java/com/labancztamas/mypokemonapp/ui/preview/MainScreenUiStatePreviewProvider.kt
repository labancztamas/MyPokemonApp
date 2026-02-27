package com.labancztamas.mypokemonapp.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.labancztamas.mypokemonapp.model.PokemonTypes
import com.labancztamas.mypokemonapp.ui.screen.main.MainScreenInputValues
import com.labancztamas.mypokemonapp.ui.screen.main.MainScreenUiState

class MainScreenUiStatePreviewProvider :
    PreviewParameterProvider<MainScreenUiState> {

    override val values = sequenceOf(
        MainScreenUiState.Content(
            inputValues = MainScreenInputValues(
                nameText = "Pikachu",
                selectableTypes = PokemonTypes(
                    listOf("Electric", "Fire")
                ),
                selectedType = "Electric",
                isCaughtCheckBoxSelected = true
            ),
            pokemonList = listOf()
        ),
        MainScreenUiState.Initial,
        MainScreenUiState.Loading,
        MainScreenUiState.Error,
    )
}
