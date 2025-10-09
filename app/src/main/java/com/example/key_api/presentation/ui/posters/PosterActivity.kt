package com.example.key_api.presentation.ui.posters

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.key_api.R
import com.example.key_api.presentation.presenters.posters.PosterViewModel


class PosterActivity : AppCompatActivity() {
    private var viewModel: PosterViewModel? = null
    private lateinit var poster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_poster)
        poster = findViewById(R.id.ivBigPoster)
        val imageUrl = intent.extras?.getString("poster", "") ?: ""
        viewModel = ViewModelProvider(
            this,
            PosterViewModel.getFactory(imageUrl)
        ).get(PosterViewModel::class.java)

        viewModel?.observePoster()?.observe(this) {
            setupPosterImage(it.toString())
        }
    }

    private fun setupPosterImage(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .into(poster)
    }
}