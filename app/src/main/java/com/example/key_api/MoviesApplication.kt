package com.example.key_api

import android.app.Application
import com.example.key_api.di.dataModule
import com.example.key_api.di.interactorModule
import com.example.key_api.di.repositoryModule
import com.example.key_api.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoviesApplication)
            modules(listOf(dataModule, interactorModule, repositoryModule, viewModelModule))
        }
    }
}
