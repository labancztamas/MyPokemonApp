package com.labancztamas.mypokemonapp.ui.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.labancztamas.mypokemonapp.R
import com.labancztamas.mypokemonapp.ui.components.PokemonListElement
import com.labancztamas.mypokemonapp.ui.components.PokemonTypeSelector
import com.labancztamas.mypokemonapp.ui.preview.MainScreenUiStatePreviewProvider
import com.labancztamas.mypokemonapp.ui.screen.main.MainScreenContract.MainScreenAction
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainScreenContract = koinViewModel<MainScreenViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreenContent(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MainScreenContent(
    uiState: MainScreenUiState,
    onAction: (MainScreenAction) -> Unit
) {
    when (uiState) {
        MainScreenUiState.Initial -> InitialScreen(
            onAction = onAction,
        )

        is MainScreenUiState.Content -> ContentScreen(
            uiState = uiState,
            onAction = onAction,
        )

        MainScreenUiState.Loading -> LoadingScreen()
        MainScreenUiState.Error -> ErrorScreen(
            resetScreen = { onAction(MainScreenAction.InitializeScreen) }
        )
    }
}

@Composable
private fun InitialScreen(
    onAction: (MainScreenAction) -> Unit
) {
    LaunchedEffect(Unit) {
        onAction(MainScreenAction.InitializeScreen)
    }
}

@Composable
private fun LoadingScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorScreen(resetScreen: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("An error has occurred.")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = resetScreen) {
            Text("Reload")
        }
    }
}

@Composable
private fun ContentScreen(
    uiState: MainScreenUiState.Content,
    onAction: (MainScreenAction) -> Unit,
) {
    Column {
        PokemonTitle()

        InputFieldsSection(
            uiState = uiState,
            onAction = onAction
        )

        PokemonResultList(
            uiState = uiState,
            onAction = onAction
        )
    }
}

@Composable
private fun PokemonTitle() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xFFCC3B3B))
    ) {
        Image(
            painter = painterResource(R.drawable.pokemon_logo),
            contentDescription = "Pokemon title",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(width = 140.dp, height = 120.dp)
                .background(Color(0xFFCC3B3B))
        )
    }
}

@Composable
private fun InputFieldsSection(
    uiState: MainScreenUiState.Content,
    onAction: (MainScreenAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 32.dp)
            .padding(top = 32.dp)
    ) {
        OutlinedTextField(
            value = uiState.inputValues.nameText ?: "",
            onValueChange = {
                onAction(MainScreenAction.SetName(it))
            },
            placeholder = {
                Text(
                    text = "Search by name..",
                    color = Color.LightGray,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (!(uiState.inputValues.selectableTypes?.types).isNullOrEmpty()) {
            PokemonTypeSelector(
                selectedType = uiState.inputValues.selectedType,
                selectableTypes = uiState.inputValues.selectableTypes,
                onTyeSelected = {
                    onAction(MainScreenAction.SetType(it))
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = uiState.inputValues.isCaughtCheckBoxSelected,
                onCheckedChange = {
                    onAction(MainScreenAction.ToggleCaughtBox(it))
                }
            )

            Text("Only show caught pokemon")
        }


        Button(
            onClick = { onAction(MainScreenAction.UpdatePokemonList) }
        ) {
            Text("Search")
        }
    }
}

@Composable
private fun PokemonResultList(
    uiState: MainScreenUiState.Content,
    onAction: (MainScreenAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp)
            .background(Color(0xFFCDDDEE))
    ) {
        // TODO header of list, live ROOM update of list, profile screen, outsource strings
        items(items = uiState.pokemonList) { pokemon ->
            PokemonListElement(
                item = pokemon,
                onElementClick = {
                    onAction(MainScreenAction.NavigateToProfile(pokemon = pokemon))
                },
                onCatchClick = {
                    onAction(MainScreenAction.CatchPokemon(pokemon = pokemon))
                },
                onReleaseClick = {
                    onAction(MainScreenAction.ReleasePokemon(pokemon = pokemon))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(
    @PreviewParameter(MainScreenUiStatePreviewProvider::class)
    uiState: MainScreenUiState
) {
    MainScreenContent(
        uiState = uiState,
        onAction = {}
    )
}
