package com.example.key_api.presentation.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.key_api.R
import com.example.key_api.core.navigation.NavigatorHolder
import com.example.key_api.core.navigation.NavigatorImpl
import com.example.key_api.databinding.ActivityRootBinding
import com.example.key_api.presentation.ui.movies.MoviesFragment
import org.koin.android.ext.android.inject

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    private val navigatorHolder: NavigatorHolder by inject()
    private val navigator = NavigatorImpl(
        fragmentContainerViewId = R.id.rootFragmentContainerView,
        fragmentManager = supportFragmentManager
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rootView = binding.root
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }
}
