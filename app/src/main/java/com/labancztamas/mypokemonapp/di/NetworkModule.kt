package com.labancztamas.mypokemonapp.di

import com.labancztamas.mypokemonapp.network.AppHttpClient
import com.labancztamas.mypokemonapp.network.api.ApiService
import com.labancztamas.mypokemonapp.network.api.ApiServiceImpl
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {

    single<HttpClient> { AppHttpClient().client }
    singleOf(::ApiServiceImpl) { bind<ApiService>() }
}
