package com.labancztamas.mypokemonapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.labancztamas.mypokemonapp.navigation.NavigationEmitter
import com.labancztamas.mypokemonapp.navigation.Screen
import kotlinx.coroutines.launch

open class BaseViewModel(
    private val navigationEmitter: NavigationEmitter,
) : ViewModel() {

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
