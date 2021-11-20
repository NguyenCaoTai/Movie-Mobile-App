package com.sfg.moviemobileapp.ui.movie.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sfg.moviemobileapp.data.api.MovieApi
import com.sfg.moviemobileapp.data.repository.MovieRepository
import com.sfg.moviemobileapp.data.repository.model.MovieItem
import com.sfg.moviemobileapp.data.repository.model.MovieType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class MovieListViewModel(
    private val repository: MovieRepository,
) : ViewModel() {
    val loadEvent = MutableSharedFlow<MovieType>(1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts = loadEvent
        .flatMapLatest {
            when (it) {
                MovieType.NowPlaying -> repository.moviePlaying()
                MovieType.TopRated -> repository.movieTopRate()
            }
        }
        .cachedIn(viewModelScope)
}
