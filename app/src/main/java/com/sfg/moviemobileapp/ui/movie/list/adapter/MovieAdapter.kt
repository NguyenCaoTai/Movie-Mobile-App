package com.sfg.moviemobileapp.ui.movie.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sfg.moviemobileapp.data.api.dto.Movie
import com.sfg.moviemobileapp.data.repository.model.MovieItem

class MovieAdapter(
    private val onItemClickListener: View.OnClickListener
) : PagingDataAdapter<MovieItem, MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder
            .apply {
                bind(getItem(position))
                itemView.setOnClickListener(onItemClickListener)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.create(parent)
    }

    companion object {
        val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem.id == newItem.id

            override fun getChangePayload(oldItem: MovieItem, newItem: MovieItem): Any? {
                return null
            }
        }
    }
}
