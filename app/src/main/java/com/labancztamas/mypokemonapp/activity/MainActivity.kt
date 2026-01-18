package com.labancztamas.mypokemonapp.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.labancztamas.mypokemonapp.ui.theme.MyPokemonAppTheme

class MainActivity : NavigatorActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPokemonAppTheme {
                Navigator()
            }
        }
    }
}