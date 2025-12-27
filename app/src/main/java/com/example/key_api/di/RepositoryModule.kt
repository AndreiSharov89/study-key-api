package com.example.key_api.di

import com.example.key_api.data.MoviesRepositoryImpl
import com.example.key_api.data.converter.MovieCastConverter
import com.example.key_api.data.storage.NamesRepositoryImpl
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.domain.api.NamesRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory { MovieCastConverter() }

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get())
    }

    single<NamesRepository> {
        NamesRepositoryImpl(get())
    }
}