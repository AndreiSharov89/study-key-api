package com.example.key_api

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val imdbBaseUrl = "https://tv-api.com"
    private val key: String = BuildConfig.API_KEY

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val imdbService = retrofit.create(ImdbApi::class.java)

    private val movies = ArrayList<Movie>()

    private val adapter = MoviesAdapter()

    private lateinit var searchButton: Button
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView

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

        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        placeholderMessage = findViewById(R.id.placeholderMessage)
        searchButton = findViewById(R.id.searchButton)
        queryInput = findViewById(R.id.queryInput)
        moviesList = findViewById(R.id.movies)
        adapter.movies = movies
        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        searchButton.setOnClickListener {
            if (queryInput.text.isNotEmpty()) {
                search()
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
        }
    }


    private fun search() {
        imdbService.getMovies(key, queryInput.text.toString())
            .enqueue(object : Callback<MoviesResponse> {
                override fun onResponse(
                    call: Call<MoviesResponse>,
                    response: Response<MoviesResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            movies.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                movies.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            }
                            if (movies.isEmpty()) {
                                showMessage(getString(R.string.nothing_found), "")
                            } else {
                                showMessage("", "")
                            }
                        }

                        401 -> showMessage(
                            getString(R.string.unauthorized),
                            response.code().toString()
                        )

                        else -> showMessage(
                            getString(R.string.something_went_wrong),
                            response.code().toString()
                        )
                    }

                }

                override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    showMessage(getString(R.string.something_went_wrong), t.message.toString())
                }

            })
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholderMessage.visibility = View.VISIBLE
            movies.clear()
            adapter.notifyDataSetChanged()
            placeholderMessage.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }
}