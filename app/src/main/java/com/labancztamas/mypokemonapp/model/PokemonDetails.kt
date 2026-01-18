package com.labancztamas.mypokemonapp.model

data class PokemonDetails(
    val notHiddenAbilities: List<String>,
    val height: Int,
    val id: Int,
    val name: String,
    val imageUrl: String,
    val weight: Int
)

