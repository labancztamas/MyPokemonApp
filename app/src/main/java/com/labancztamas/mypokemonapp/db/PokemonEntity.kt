package com.labancztamas.mypokemonapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity(
    @PrimaryKey val name: String,
    val type: String,
)