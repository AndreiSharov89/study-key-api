package com.example.key_api.presentation.ui.cast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.key_api.R
import com.example.key_api.databinding.ActivityMoviesCastBinding

class MoviesCastActivity : AppCompatActivity(R.layout.activity_movies_cast) {

    companion object {
        private lateinit var binding: ActivityMoviesCastBinding
        private const val ARGS_MOVIE_ID = "movie_id"

        fun newInstance(context: Context, movieId: String): Intent {
            return Intent(context, MoviesCastActivity::class.java).apply {
                putExtra(ARGS_MOVIE_ID, movieId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMoviesCastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // TODO "Добавить вёрстку"
        // TODO "Прочитать идентификатор фильма из Intent"
    }

}