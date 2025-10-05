package com.example.key_api.presentation.presenters.movies

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MoviesView : MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: MoviesState)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(additionalMessage: String)

}