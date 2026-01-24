package com.labancztamas.mypokemonapp.ui.screen.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.labancztamas.mypokemonapp.R
import com.labancztamas.mypokemonapp.model.PokemonDetails
import com.labancztamas.mypokemonapp.ui.components.ErrorScreen
import com.labancztamas.mypokemonapp.ui.components.LoadingScreen
import com.labancztamas.mypokemonapp.ui.components.MyPokemonAppScaffold
import com.labancztamas.mypokemonapp.ui.components.PokemonDetailElement
import com.labancztamas.mypokemonapp.ui.preview.ProfileScreenUiStatePreviewProvider
import com.labancztamas.mypokemonapp.ui.screen.profile.ProfileScreenContract.ProfileScreenAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    pokemonName: String,
    viewModel: ProfileScreenContract = koinViewModel<ProfileScreenViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreenContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        pokemonName = pokemonName,
    )
}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileScreenUiState,
    onAction: (ProfileScreenAction) -> Unit,
    pokemonName: String
) {
    when (uiState) {
        is ProfileScreenUiState.Content -> ContentScreen(
            uiState = uiState,
            onAction = onAction,
        )

        ProfileScreenUiState.Error -> ErrorScreen(
            resetScreen = {
                onAction(ProfileScreenAction.InitializeScreen(name = pokemonName))
            }
        )

        ProfileScreenUiState.Initial -> InitialScreen(
            pokemonName = pokemonName,
            onAction = onAction,
        )

        ProfileScreenUiState.Loading -> LoadingScreen()
    }

    BackHandler {
        onAction(ProfileScreenAction.NavigateBack)
    }
}

@Composable
private fun InitialScreen(
    pokemonName: String,
    onAction: (ProfileScreenAction) -> Unit
) {
    LaunchedEffect(Unit) {
        onAction(ProfileScreenAction.InitializeScreen(name = pokemonName))
    }
}

@Composable
private fun ContentScreen(
    uiState: ProfileScreenUiState.Content,
    onAction: (ProfileScreenAction) -> Unit
) {
    val pokemonDetails = uiState.pokemonDetails

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PokemonImage(pokemonDetails = pokemonDetails)

        Spacer(modifier = Modifier.height(24.dp))

        PokemonDetailsList(
            pokemonDetails = pokemonDetails
        )

        Spacer(modifier = Modifier.height(24.dp))

        CatchOrReleaseButton(pokemonDetails, onAction)
    }
}

@Composable
private fun PokemonImage(
    pokemonDetails: PokemonDetails
) {
    AsyncImage(
        model = pokemonDetails.imageUrl,
        contentDescription = "Pokemon image",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                color = if (pokemonDetails.isCaught) {
                    colorResource(R.color.yellow)
                } else {
                    colorResource(R.color.blue)
                },
                width = 3.dp
            )
            .padding(bottom = 20.dp)
    )
}

// TODO outsource strings
@Composable
private fun PokemonDetailsList(pokemonDetails: PokemonDetails) {
    Column {
        PokemonDetailElement(
            key = "Name",
            values = listOf(pokemonDetails.name),
            indexInList = 0
        )

        PokemonDetailElement(
            key = "Weight",
            values = listOf(pokemonDetails.weight.toString() + " kg"),
            indexInList = 1
        )

        PokemonDetailElement(
            key = "Height",
            values = listOf(pokemonDetails.height.toString() + " m"),
            indexInList = 2
        )

        PokemonDetailElement(
            key = "Abilities",
            values = pokemonDetails.notHiddenAbilities,
            indexInList = 3
        )

        PokemonDetailElement(
            key = "Status",
            values = listOf(
                if (pokemonDetails.isCaught) {
                    "Caught"
                } else {
                    "-"
                }
            ),
            indexInList = 4
        )
    }
}

@Composable
private fun CatchOrReleaseButton(
    pokemonDetails: PokemonDetails,
    onAction: (ProfileScreenAction) -> Unit
) {
    val isCaught = pokemonDetails.isCaught

    Button(
        onClick = {
            if (isCaught) {
                onAction(ProfileScreenAction.ReleasePokemon)
            } else {
                onAction(ProfileScreenAction.CatchPokemon)
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isCaught) {
                colorResource(R.color.yellow)
            } else {
                colorResource(R.color.blue)
            }
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = if (isCaught) {
                "Release"
            } else {
                "Catch"
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(
    @PreviewParameter(ProfileScreenUiStatePreviewProvider::class)
    uiState: ProfileScreenUiState
) {
    MyPokemonAppScaffold {
        ProfileScreenContent(
            uiState = uiState,
            onAction = {},
            pokemonName = "pikachu"
        )
    }
}
