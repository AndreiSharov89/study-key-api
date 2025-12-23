package com.example.key_api.core.navigation

import androidx.fragment.app.Fragment

interface NavigatorHolder {
    fun attachNavigator(navigator: Navigator)

    fun detachNavigator()

    fun openFragment(fragment: Fragment)
}