package com.example.key_api.presentation.ui.movies

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.key_api.Creator
import com.example.key_api.R
import com.example.key_api.domain.api.MoviesInteractor
import com.example.key_api.domain.models.Movie
import com.example.key_api.presentation.ui.posters.PosterActivity

class MoviesActivity : AppCompatActivity() {

    private val movies = ArrayList<Movie>()
    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private val interactor: MoviesInteractor = Creator.provideMoviesInteractor()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    private lateinit var searchButton: Button
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar

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

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        placeholderMessage = findViewById(R.id.placeholderMessage)
        searchButton = findViewById(R.id.searchButton)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.movies)
        progressBar = findViewById(R.id.progressBar)

        adapter.movies = movies
        moviesList.layoutManager = LinearLayoutManager(this)
        moviesList.adapter = adapter

        searchButton.setOnClickListener {
            if (queryInput.text.isNotEmpty()) {
                search()
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
        }

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }


    private fun search() {
        val expression = queryInput.text.toString()
        if (expression.isEmpty()) return

        placeholderMessage.visibility = View.GONE
        moviesList.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        progressBar.progress = 40

        interactor.searchMovies(expression, object : MoviesInteractor.MoviesConsumer {
            override fun consume(foundMovies: List<Movie>) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    val oldSize = movies.size
                    movies.clear()
                    adapter.notifyItemRangeRemoved(0, oldSize)
                    movies.addAll(foundMovies)
                    adapter.notifyItemRangeInserted(0, foundMovies.size)

                    if (foundMovies.isEmpty()) {
                        showMessage(getString(R.string.nothing_found))
                    } else {
                        placeholderMessage.visibility = View.GONE
                        moviesList.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun showMessage(text: String, toastMessage: String? = null) {
        placeholderMessage.text = text
        placeholderMessage.visibility = View.VISIBLE
        val size = movies.size
        movies.clear()
        adapter.notifyItemRangeRemoved(0, size)
        toastMessage?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
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

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}