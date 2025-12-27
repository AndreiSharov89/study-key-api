package com.example.key_api.di

import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.api.NamesInteractor
import com.example.key_api.domain.api.SearchHistoryInteractor
import com.example.key_api.domain.impl.MoviesInteractorImpl
import com.example.key_api.domain.impl.NamesInteracrorImpl
import com.example.key_api.domain.impl.SearchHistoryInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<MoviesInteractor> {
        MoviesInteractorImpl(get())
    }
    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }
    single<NamesInteractor> {
        NamesInteracrorImpl(get())
    }
}