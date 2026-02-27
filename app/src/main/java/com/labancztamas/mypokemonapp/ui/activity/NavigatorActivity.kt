package com.labancztamas.mypokemonapp.ui.activity

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.labancztamas.mypokemonapp.navigation.NavigationEvent
import com.labancztamas.mypokemonapp.navigation.NavigationHandler
import com.labancztamas.mypokemonapp.navigation.NavigationHost
import com.labancztamas.mypokemonapp.utils.collectWithLifecycle
import org.koin.android.ext.android.get

open class NavigatorActivity : ComponentActivity() {

    private var navigationHandler: NavigationHandler = get()

    @Composable
    protected fun Navigator() {
        val navController = rememberNavController()

        NavigationHost(
            navController = navController
        )

        NavigationEventHandler(navController = navController)
    }

    @Composable
    private fun NavigationEventHandler(
        navController: NavHostController
    ) {
        navigationHandler.navigationEvents.collectWithLifecycle(
            lifecycleOwner = this@NavigatorActivity
        ) { navEvent ->
            when (navEvent) {
                is NavigationEvent.Forward -> {
                    navController.popBackStack(route = navEvent.screen, inclusive = true)
                    navController.navigate(navEvent.screen)
                }

                is NavigationEvent.Back -> {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } else {
                        this@NavigatorActivity.finish()
                    }

                }
            }
        }
    }
}
