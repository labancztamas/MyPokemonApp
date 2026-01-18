package com.labancztamas.mypokemonapp.navigation

import kotlinx.coroutines.flow.Flow

interface NavigationHandler {

    val navigationEvents: Flow<NavigationEvent>
}
