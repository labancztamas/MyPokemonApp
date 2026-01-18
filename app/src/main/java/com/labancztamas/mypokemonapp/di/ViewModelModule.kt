package com.labancztamas.mypokemonapp.di

import com.labancztamas.mypokemonapp.ui.screen.main.MainScreenContract
import com.labancztamas.mypokemonapp.ui.screen.main.MainScreenViewModel
import com.labancztamas.mypokemonapp.ui.screen.profile.ProfileScreenContract
import com.labancztamas.mypokemonapp.ui.screen.profile.ProfileScreenViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainScreenViewModel) { bind<MainScreenContract>() }
    viewModelOf(::ProfileScreenViewModel) { bind<ProfileScreenContract>() }
}
