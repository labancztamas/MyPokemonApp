package com.labancztamas.mypokemonapp.di

import org.koin.dsl.module

val applicationModule = module {
    includes(
        navigationModule,
        viewModelModule,
        databaseModule,
        networkModule,
        repositoryModule,
    )
}
