package com.labancztamas.mypokemonapp.di

import com.labancztamas.mypokemonapp.repository.PokemonRepository
import com.labancztamas.mypokemonapp.repository.PokemonRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::PokemonRepositoryImpl) { bind<PokemonRepository>() }
}
