package com.labancztamas.mypokemonapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labancztamas.mypokemonapp.navigation.NavigationEmitter
import com.labancztamas.mypokemonapp.navigation.Screen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel(
    private val navigationEmitter: NavigationEmitter,
) : ViewModel() {

    fun launchInViewModelScope(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            withContext(dispatcher) {
                block()
            }
        }
    }

    fun navigateTo(screen: Screen) {
        viewModelScope.launch {
            navigationEmitter.navigateTo(screen)
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            navigationEmitter.navigateBack()
        }
    }
}
