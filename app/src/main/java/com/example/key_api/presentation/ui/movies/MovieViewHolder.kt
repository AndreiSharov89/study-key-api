package com.example.key_api.presentation.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.key_api.databinding.MovieViewBinding
import com.example.key_api.domain.models.Movie

class MovieViewHolder(private val binding: MovieViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie){
        binding.movieTitle.text = movie.title
        binding.movieDescription.text = movie.description
        Glide.with(itemView)
            .load(movie.image)
            .centerCrop()
            .into(binding.movieImage)
    }

    companion object {
        fun from(parent: ViewGroup): MovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MovieViewBinding.inflate(inflater, parent, false)
            return MovieViewHolder(binding)
        }
    }
}