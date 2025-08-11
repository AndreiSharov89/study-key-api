package com.example.key_api.presentation.ui.posters

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.key_api.Creator
import com.example.key_api.R

class PosterActivity : AppCompatActivity() {
    private val posterController = Creator.providePosterController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_poster)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        posterController.onCreate()
        val posterUrl = intent.getStringExtra("poster")
        val poster: ImageView = findViewById(R.id.ivBigPoster)

        Glide.with(this)
            .load(posterUrl)
            .into(poster)
    }
}