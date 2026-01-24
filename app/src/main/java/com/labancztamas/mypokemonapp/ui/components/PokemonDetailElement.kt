package com.labancztamas.mypokemonapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labancztamas.mypokemonapp.R

@Composable
fun PokemonDetailElement(
    key: String,
    values: List<String>,
    indexInList: Int,
) {
    // TODO make these colors lighter
    val backgroundColor = if (indexInList % 2 == 0) {
        colorResource(R.color.blue)
    } else {
        colorResource(R.color.yellow)
    }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .heightIn(min = 32.dp)
    ) {
        Text(
            text = key,
            color = Color.DarkGray
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (value in values) {
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Preview
@Composable
private fun PokemonDetailElementPreview() {
    PokemonDetailElement(
        key = "Abilities",
        values = listOf("static", "lightning-rod"),
        indexInList = 1
    )
}
