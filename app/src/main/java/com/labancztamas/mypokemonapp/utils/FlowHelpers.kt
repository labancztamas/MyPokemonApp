package com.labancztamas.mypokemonapp.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

const val STATEFLOW_SUBSCRIPTION_TIME = 5000L

@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.collectWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    repeatOnState: Lifecycle.State = Lifecycle.State.STARTED,
    block: (T) -> Unit
) {
    LaunchedEffect(lifecycleOwner.lifecycleScope, this) {
        lifecycleOwner.repeatOnLifecycle(repeatOnState) {
            withContext(Dispatchers.Main.immediate) {
                this@collectWithLifecycle.collect {
                    block(it)
                }
            }
        }
    }
}
