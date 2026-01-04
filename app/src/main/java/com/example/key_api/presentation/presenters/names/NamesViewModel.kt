package com.example.key_api.presentation.presenters.names

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.key_api.R
import com.example.key_api.domain.api.NamesInteractor
import com.example.key_api.domain.models.Person
import com.example.key_api.presentation.SingleLiveEvent
import com.example.key_api.util.debounce
import kotlinx.coroutines.launch

class NamesViewModel(
    private val context: Context,
    private val namesInteractor: NamesInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val stateLiveData = MutableLiveData<NamesState>()
    fun observeState(): LiveData<NamesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null

    private val movieSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchRequest(changedText)
        }
    fun searchDebounce(changedText: String) {

        if (latestSearchText != changedText) {
            latestSearchText = changedText
            movieSearchDebounce(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(NamesState.Loading)

            // Launch a coroutine to call the suspend function
            viewModelScope.launch {
                namesInteractor.searchNames(newSearchText, object : NamesInteractor.NamesConsumer {
                    override fun consume(foundNames: List<Person>?, errorMessage: String?) {
                        val persons = mutableListOf<Person>()
                        if (foundNames != null) {
                            persons.addAll(foundNames)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    NamesState.Error(
                                        message = context.getString(R.string.something_went_wrong),
                                    )
                                )
                                showToast.postValue(errorMessage)
                            }

                            persons.isEmpty() -> {
                                renderState(
                                    NamesState.Empty(message = context.getString(R.string.nothing_found))
                                )
                            }

                            else -> {
                                renderState(NamesState.Content(persons = persons))
                            }
                        }
                    }
                })
            }
        }
    }

    private fun renderState(state: NamesState) {
        stateLiveData.postValue(state)
    }
}