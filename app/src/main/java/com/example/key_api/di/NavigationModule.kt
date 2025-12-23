package com.example.key_api.di

import com.example.key_api.core.navigation.Router
import com.example.key_api.core.navigation.RouterImpl
import org.koin.dsl.module

val navigationModule = module {
    val router = RouterImpl()

    single<Router> { router }
    single { router.navigatorHolder }
}