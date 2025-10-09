package com.example.key_api.presentation.presenters.posters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class PosterViewModel(private val imageUrl: String) : ViewModel() {
    companion object {
        fun getFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PosterViewModel(url)
            }
        }
    }

    private val posterLiveData = MutableLiveData<String>(imageUrl)
    fun observePoster(): LiveData<String> = posterLiveData

}