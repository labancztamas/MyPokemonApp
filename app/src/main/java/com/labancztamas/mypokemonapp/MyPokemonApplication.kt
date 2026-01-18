package com.labancztamas.mypokemonapp

import android.app.Application
import com.labancztamas.mypokemonapp.di.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyPokemonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyPokemonApplication)
            modules(applicationModule)
        }
    }
}
