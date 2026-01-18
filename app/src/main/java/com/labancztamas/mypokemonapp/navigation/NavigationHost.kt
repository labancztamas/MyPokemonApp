package com.labancztamas.mypokemonapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.labancztamas.mypokemonapp.ui.screen.main.MainScreen
import com.labancztamas.mypokemonapp.ui.screen.profile.ProfileScreen

@Composable
internal fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen
    ) {
        composable<Screen.MainScreen> {
            MainScreen()
        }

        composable<Screen.ProfileScreen> { entry ->
            val route = entry.toRoute<Screen.ProfileScreen>()

            ProfileScreen(
                pokemonName = route.pokemonName,
            )
        }
    }
}
