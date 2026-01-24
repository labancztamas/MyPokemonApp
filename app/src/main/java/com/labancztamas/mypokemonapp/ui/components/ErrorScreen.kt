package com.labancztamas.mypokemonapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ErrorScreen(resetScreen: () -> Unit) {
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


@Preview(showBackground = true)
@Composable
private fun LoadingScreenPreview() {
    MyPokemonAppScaffold {
        ErrorScreen(
            resetScreen = {}
        )
    }
}
