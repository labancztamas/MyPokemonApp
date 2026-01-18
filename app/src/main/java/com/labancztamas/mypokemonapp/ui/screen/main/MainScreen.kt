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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        MainScreenUiState.Error -> ErrorScreen(viewModel::initalizeScreen)
        is MainScreenUiState.Loaded -> LoadedScreen(
            uiState = uiState as MainScreenUiState.Loaded,
            onTextChanged = viewModel::setName,
            updateList = viewModel::updatePokemonList,
            selectType = viewModel::setType,
        )

        MainScreenUiState.Loading -> LoadingScreen()
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
    uiState: MainScreenUiState.Loaded,
    onTextChanged: (String) -> Unit,
    updateList: () -> Unit,
    selectType: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = uiState.inputValues.nameText ?: "",
            onValueChange = onTextChanged,
            modifier = Modifier.onFocusChanged { if (!it.isFocused) updateList() }
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
                            onClick = { selectType(type) }
                        )
                }
            }
        }
    }
}