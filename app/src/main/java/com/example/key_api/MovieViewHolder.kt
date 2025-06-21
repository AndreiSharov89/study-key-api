package com.example.key_api

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_view, parent, false)) {
    private val title: TextView = itemView.findViewById(R.id.movie_title)
    private val description: TextView = itemView.findViewById(R.id.movie_description)
    private val trackImage: ImageView = itemView.findViewById(R.id.movie_image)

    fun bind(movie: Movie){
        title.text = movie.title
        description.text = movie.description
        Glide.with(itemView)
            .load(movie.image)
            .centerCrop()
            .into(trackImage)
    }
}