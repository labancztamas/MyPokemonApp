package com.labancztamas.mypokemonapp.navigation

interface NavigationEmitter {

    suspend fun navigateTo(screen: Screen)
    suspend fun navigateBack()
    suspend fun navigateBackTo(screen: Screen)
}
