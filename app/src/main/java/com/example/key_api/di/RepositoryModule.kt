package com.example.key_api.di

import com.example.key_api.data.MoviesRepositoryImpl
import com.example.key_api.data.SearchHistoryRepositoryImpl
import com.example.key_api.domain.api.MoviesRepository
import com.example.key_api.domain.api.SearchHistoryRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<MoviesRepository> {
        MoviesRepositoryImpl(get())
    }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }
}