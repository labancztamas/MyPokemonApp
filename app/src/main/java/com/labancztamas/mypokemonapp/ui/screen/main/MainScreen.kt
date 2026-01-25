package com.labancztamas.mypokemonapp.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.labancztamas.mypokemonapp.R
import com.labancztamas.mypokemonapp.ui.components.ErrorScreen
import com.labancztamas.mypokemonapp.ui.components.LoadingScreen
import com.labancztamas.mypokemonapp.ui.components.MyPokemonAppScaffold
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

    BackHandler {
        onAction(MainScreenAction.NavigateBack)
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
private fun ContentScreen(
    uiState: MainScreenUiState.Content,
    onAction: (MainScreenAction) -> Unit,
) {
    LaunchedEffect(Unit) {
        onAction(MainScreenAction.CheckCaughtPokemons)
    }

    Column {
        InputFieldsSection(
            uiState = uiState,
            onAction = onAction
        )

        if (uiState.pokemonList.isNotEmpty()) {
            PokemonResultList(
                uiState = uiState,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun InputFieldsSection(
    uiState: MainScreenUiState.Content,
    onAction: (MainScreenAction) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onAction(
                        MainScreenAction.ToggleCaughtBox(
                            !uiState.inputValues.isCaughtCheckBoxSelected
                        )
                    )
                }
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
            onClick = { onAction(MainScreenAction.UpdatePokemonList) },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.red)
            )
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
            .fillMaxHeight()
            .padding(top = 16.dp)
            .background(colorResource(R.color.light_blue))
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 60.dp)
                    .height(48.dp)
            ) {
                Text("Name")
                Text("Type")
                Text("Status")
            }
        }
        items(
            items = uiState.pokemonList,
            key = { it.name }
        ) { pokemon ->
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
    MyPokemonAppScaffold {
        MainScreenContent(
            uiState = uiState,
            onAction = {}
        )
    }
}
