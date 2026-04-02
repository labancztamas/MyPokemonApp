package com.labancztamas.mypokemonapp.di

import androidx.room.Room
import com.labancztamas.mypokemonapp.db.PokemonDao
import com.labancztamas.mypokemonapp.db.PokemonDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<PokemonDao> {
        Room.databaseBuilder(
            androidContext(),
            PokemonDatabase::class.java, "pokemons"
        )
            .build()
            .pokemonDao()
    }
}
