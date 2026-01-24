package com.labancztamas.mypokemonapp.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.labancztamas.mypokemonapp.model.PokemonDetails
import com.labancztamas.mypokemonapp.ui.screen.profile.ProfileScreenUiState

private const val PREVIEW_IMAGE_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"

class ProfileScreenUiStatePreviewProvider :
    PreviewParameterProvider<ProfileScreenUiState> {

    override val values = sequenceOf(
        ProfileScreenUiState.Content(
            pokemonDetails = PokemonDetails(
                notHiddenAbilities = listOf("static, lightning-rod"),
                height = 4,
                name = "Pikachu",
                imageUrl = PREVIEW_IMAGE_URL,
                weight = 60,
                type = "electric",
                isCaught = true,
            )
        ),
        ProfileScreenUiState.Initial,
        ProfileScreenUiState.Loading,
        ProfileScreenUiState.Error,
    )
}
