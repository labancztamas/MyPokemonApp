package com.labancztamas.mypokemonapp.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

        is MainScreenUiState.Content -> LoadedScreen(
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
private fun LoadedScreen(
    uiState: MainScreenUiState.Content,
    onAction: (MainScreenAction) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = uiState.inputValues.nameText ?: "",
            onValueChange = {
                onAction(MainScreenAction.SetName(it))
            },
            modifier = Modifier.onFocusChanged {
                if (!it.isFocused) {
                    onAction(MainScreenAction.UpdatePokemonList)
                }
            }
        )

        if (!(uiState.inputValues.selectableTypes?.types).isNullOrEmpty()) {
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(enabled = true) {
                        expanded = !expanded
                    }
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    for (type in uiState.inputValues.selectableTypes.types)
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = { onAction(MainScreenAction.SetType(type)) }
                        )
                }
            }
        }

        Button(
            onClick = { onAction(MainScreenAction.UpdatePokemonList) }
        ) {
            Text("Search")
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
