package com.example.key_api.presentation.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.key_api.R
import com.example.key_api.domain.models.Movie
import com.example.key_api.presentation.presenters.movies.MoviesSearchPresenter
import com.example.key_api.presentation.presenters.movies.MoviesState
import com.example.key_api.presentation.presenters.movies.MoviesViewModel
import com.example.key_api.presentation.ui.posters.PosterActivity

class MoviesActivity : AppCompatActivity() {
    private var viewModel: MoviesViewModel? = null
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var movies: RecyclerView
    private lateinit var progressBar: ProgressBar
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

    private var moviesSearchPresenter: MoviesSearchPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rootView = findViewById<LinearLayout>(R.id.main_root_view)
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
        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        movies = findViewById(R.id.movies)
        progressBar = findViewById(R.id.progressBar)
        movies.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        movies.adapter = adapter

        viewModel = ViewModelProvider(this, MoviesViewModel.getFactory())
            .get(MoviesViewModel::class.java)

        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        viewModel?.observeShowToast()?.observe(this) {
            showToast(it)
        }


        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel?.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

    fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
        }
    }

    fun showLoading() {
        movies.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showError(errorMessage: String) {
        movies.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = errorMessage
    }

    fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    fun showContent(movies: List<Movie>) {
        movies.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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