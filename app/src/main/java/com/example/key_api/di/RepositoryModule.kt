package com.example.key_api.di

import com.example.key_api.data.HistoryRepositoryImpl
import com.example.key_api.data.MoviesRepositoryImpl
import com.example.key_api.data.converter.MovieCastConverter
import com.example.key_api.data.converter.MovieDbConverter
import com.example.key_api.data.storage.NamesRepositoryImpl
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.domain.api.NamesRepository
import com.example.key_api.domain.db.HistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory { MovieCastConverter() }

    factory { MovieDbConverter() }

    single<MoviesRepository> {
        MoviesRepositoryImpl(get(), get(), get(), get())
    }

    single<NamesRepository> {
        NamesRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

}