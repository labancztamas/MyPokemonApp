package com.labancztamas.mypokemonapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labancztamas.mypokemonapp.R
import com.labancztamas.mypokemonapp.model.PokemonListItem

@Composable
fun PokemonListElement(
    item: PokemonListItem,
    onElementClick: () -> Unit,
    onCatchClick: () -> Unit,
    onReleaseClick: () -> Unit
) {
    val isCaught = item.isCaught
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isCaught) {
                        colorResource(R.color.yellow)
                    } else {
                        colorResource(R.color.blue)
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(onClick = onElementClick)
                .height(40.dp)
                .weight(1f)
        ) {
            Text(
                text = item.name,
                color = Color.DarkGray,
            )
            Text(
                text = item.type,
                color = Color.DarkGray
            )
            Text(
                text = if (isCaught) {
                    "Caught"
                } else {
                    "-"
                },
                color = Color.DarkGray,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = if (isCaught) {
                onReleaseClick
            } else {
                onCatchClick
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isCaught) {
                    colorResource(R.color.yellow)
                } else {
                    colorResource(R.color.blue)
                }
            )
        ) {
            Text(
                text = if (isCaught) {
                    "Release"
                } else {
                    "Catch"
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonListElementPreview() {
    MaterialTheme {
        PokemonListElement(
            item = PokemonListItem(
                type = "electric",
                name = "Pikachu",
                isCaught = true
            ),
            onElementClick = {},
            onCatchClick = {},
            onReleaseClick = {}
        )
    }
}
