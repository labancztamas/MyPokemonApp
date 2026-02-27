package com.labancztamas.mypokemonapp.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.labancztamas.mypokemonapp.ui.components.MyPokemonAppScaffold

class MainActivity : NavigatorActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPokemonAppScaffold {
                Navigator()
            }
        }
    }
}
