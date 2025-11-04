package com.example.key_api.presentation.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.key_api.databinding.ActivityMainBinding
import com.example.key_api.domain.models.Movie
import com.example.key_api.presentation.presenters.movies.MoviesState
import com.example.key_api.presentation.presenters.movies.MoviesViewModel
import com.example.key_api.presentation.ui.posters.PosterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesActivity : AppCompatActivity() {
    private val viewModel by viewModel<MoviesViewModel>()
    private lateinit var binding: ActivityMainBinding
    private var textWatcher: TextWatcher? = null

    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        binding.movies.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.movies.adapter = adapter

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeShowToast().observe(this) {
            showToast(it)
        }


        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { binding.queryInput.addTextChangedListener(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.queryInput.removeTextChangedListener(it) }
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
        }
    }

    private fun showLoading() {
        binding.apply {
            movies.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError(errorMessage: String) {
        binding.apply {
            movies.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            placeholderMessage.text = errorMessage
        }
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(moviesList: List<Movie>) {
        binding.apply {
            movies.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            adapter.movies.clear()
            adapter.movies.addAll(moviesList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showToast(message: String?) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}