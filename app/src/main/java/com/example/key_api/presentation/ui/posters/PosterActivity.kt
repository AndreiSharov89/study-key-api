package com.example.key_api.presentation.ui.posters

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.key_api.R
import com.example.key_api.presentation.presenters.posters.PosterPresenter
import com.example.key_api.presentation.presenters.posters.PosterView
import com.example.key_api.util.Creator

class PosterActivity : AppCompatActivity(), PosterView {
    private lateinit var posterPresenter: PosterPresenter
    private lateinit var poster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_poster)
        val imageUrl = intent.extras?.getString("poster", "") ?: ""
        posterPresenter = Creator.providePosterPresenter(this, imageUrl)

        setContentView(R.layout.activity_poster)
        poster = findViewById(R.id.ivBigPoster)
        posterPresenter.onCreate()
    }

    override fun setupPosterImage(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .into(poster)
    }
}