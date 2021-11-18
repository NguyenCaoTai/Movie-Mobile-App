package com.sfg.moviemobileapp.ui.movie.list.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sfg.moviemobileapp.data.api.Movie

class MovieAdapter
    : PagingDataAdapter<Movie, MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent)
    }

    companion object {
        val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun getChangePayload(oldItem: Movie, newItem: Movie): Any? {
                return null
            }
        }
    }
}
