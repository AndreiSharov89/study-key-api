package com.example.key_api.presentation.ui.posters

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.key_api.R
import com.example.key_api.util.Creator

class PosterActivity : AppCompatActivity() {
    private val posterController = Creator.providePosterController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_poster)
        posterController.onCreate()
    }
}