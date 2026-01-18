package com.labancztamas.mypokemonapp.navigation

sealed interface NavigationEvent {

    data class Forward(val screen: Screen) : NavigationEvent

    data class Back(val screen: Screen? = null) : NavigationEvent
}
