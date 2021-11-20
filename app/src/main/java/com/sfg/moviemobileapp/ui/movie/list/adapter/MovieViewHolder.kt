package com.sfg.moviemobileapp.ui.movie.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sfg.moviemobileapp.R
import com.sfg.moviemobileapp.data.api.dto.Movie
import com.sfg.moviemobileapp.data.repository.model.MovieItem

class MovieViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.title)
    private val releaseDate: TextView = view.findViewById(R.id.release_date)
    private val overview: TextView = view.findViewById(R.id.overview)
    private val voteAverage: TextView = view.findViewById(R.id.vote_average)
    private val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    private var movie: MovieItem? = null

    fun bind(movie: MovieItem?) {
        this.movie = movie
        itemView.tag = movie?.id
        title.text = movie?.title
        releaseDate.text = movie?.releasedYear
        overview.text = movie?.overview
        voteAverage.text = movie?.voteAverage
        movie?.poster
            ?.let {
                Glide.with(itemView)
                    .load(it)
                    .into(thumbnail)
            }
    }

    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
            return MovieViewHolder(view)
        }
    }
}