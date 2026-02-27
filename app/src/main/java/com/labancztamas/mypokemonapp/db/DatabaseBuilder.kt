package com.labancztamas.mypokemonapp.db

import android.content.Context
import androidx.room.Room

class DatabaseBuilder(
    private val context: Context,
) {
    fun getDao() = Room.databaseBuilder(
        context,
        PokemonDatabase::class.java, "pokemons"
    )
        .build()
        .pokemonDao()
}
