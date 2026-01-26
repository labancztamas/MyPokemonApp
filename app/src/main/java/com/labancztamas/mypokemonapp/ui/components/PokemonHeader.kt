package com.labancztamas.mypokemonapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labancztamas.mypokemonapp.R

@Composable
fun PokemonHeader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(colorResource(R.color.red))
    ) {
        Image(
            painter = painterResource(R.drawable.pokemon_logo),
            contentDescription = stringResource(
                R.string.top_app_bar_image_content_description
            ),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(width = 140.dp, height = 120.dp)
                .background(colorResource(R.color.red))
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PokemonHeaderPreview() {
    MaterialTheme {
        PokemonHeader()
    }
}
