package com.labancztamas.mypokemonapp.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class NavigationHandlerImpl : NavigationHandler, NavigationEmitter {

    private val navigationEventChannel = Channel<NavigationEvent>()

    override val navigationEvents: Flow<NavigationEvent>
        get() = navigationEventChannel.receiveAsFlow()

    override suspend fun navigateTo(screen: Screen) {
        navigationEventChannel.send(NavigationEvent.Forward(screen = screen))
    }

    override suspend fun navigateBack() {
        navigationEventChannel.send(NavigationEvent.Back())
    }

    override suspend fun navigateBackTo(screen: Screen) {
        navigationEventChannel.send(NavigationEvent.Back(screen = screen))
    }
}
