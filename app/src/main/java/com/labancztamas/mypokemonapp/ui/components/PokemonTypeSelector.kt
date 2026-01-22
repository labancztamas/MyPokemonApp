package com.labancztamas.mypokemonapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labancztamas.mypokemonapp.R
import com.labancztamas.mypokemonapp.model.PokemonTypes
import com.labancztamas.mypokemonapp.ui.theme.MyPokemonAppTheme

@Composable
fun PokemonTypeSelector(
    selectedType: String?,
    selectableTypes: PokemonTypes,
    onTyeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    val (selectedTypeText, selectedTypeColor) =
        with(selectedType) {
            if (this.isNullOrEmpty()) {
                "Select type.." to Color.LightGray
            } else {
                this to Color.Black
            }
        }

    Column(
        modifier = modifier
    ) {
        Text("Pokemon Types")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Box(
            modifier = Modifier
                .clickable(enabled = true) {
                    expanded = !expanded
                }
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(4.dp)
                )
                .height(52.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = selectedTypeText,
                    color = selectedTypeColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Icon(
                    painter = painterResource(R.drawable.arrow_drop_down_24),
                    contentDescription = "Dropdown",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                for (type in selectableTypes.types)
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onTyeSelected(type)
                            expanded = false
                        }
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonTypeSelectorPreview() {
    MyPokemonAppTheme {
        PokemonTypeSelector(
            selectedType = "Fire",
            selectableTypes = PokemonTypes(types = listOf("Fire", "Poison")),
            onTyeSelected = { _ -> }
        )
    }
}
