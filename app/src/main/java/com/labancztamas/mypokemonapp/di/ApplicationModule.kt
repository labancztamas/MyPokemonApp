package com.labancztamas.mypokemonapp.di

import androidx.room.Room
import com.labancztamas.mypokemonapp.db.PokemonDao
import com.labancztamas.mypokemonapp.db.PokemonDatabase
import com.labancztamas.mypokemonapp.interactor.PokemonInteractor
import com.labancztamas.mypokemonapp.interactor.PokemonInteractorImpl
import com.labancztamas.mypokemonapp.network.api.ApiService
import com.labancztamas.mypokemonapp.network.api.ApiServiceImpl
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val applicationModule = module {
    includes(navigationModule, viewModelModule)

    singleOf(::PokemonInteractorImpl) { bind<PokemonInteractor>() }

    single<PokemonDao> {
        Room.databaseBuilder(
            androidContext(),
            PokemonDatabase::class.java, "pokemons"
        )
            .build()
            .pokemonDao()
    }

    single<HttpClient> { HttpClient() }
    singleOf(::ApiServiceImpl) { bind<ApiService>() }
}
