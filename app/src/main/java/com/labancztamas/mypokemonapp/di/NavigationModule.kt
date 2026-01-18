package com.labancztamas.mypokemonapp.di

import com.labancztamas.mypokemonapp.navigation.NavigationEmitter
import com.labancztamas.mypokemonapp.navigation.NavigationHandler
import com.labancztamas.mypokemonapp.navigation.NavigationHandlerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module

val navigationModule = module {
    singleOf(::NavigationHandlerImpl) binds arrayOf(
        NavigationEmitter::class,
        NavigationHandler::class
    )
}
