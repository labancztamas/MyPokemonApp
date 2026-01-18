package com.labancztamas.mypokemonapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemonentity")
    fun getCaughtPokemons(): List<PokemonEntity>

    @Query("SELECT * FROM pokemonentity WHERE name IS (:nameOfPokemon)")
    fun getCaughtPokemonByName(nameOfPokemon: String): List<PokemonEntity>

    @Query("SELECT * FROM pokemonentity WHERE type IS (:typeOfPokemon)")
    fun getCaughtPokemonByType(typeOfPokemon: String): List<PokemonEntity>

    @Insert
    fun add(pokemon: PokemonEntity)

    @Delete
    fun delete(pokemon: PokemonEntity)
}