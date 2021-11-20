package com.sfg.moviemobileapp.ui.movie.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sfg.moviemobileapp.data.api.MovieApi
import com.sfg.moviemobileapp.data.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest

class MovieListViewModel(
    private val repository: MovieRepository,
) : ViewModel() {
    val loadEvent = MutableSharedFlow<MovieApi.MovieType>(1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts = loadEvent
        .flatMapLatest {
            when (it) {
                MovieApi.MovieType.NowPlaying -> repository.moviePlaying()
                MovieApi.MovieType.TopRated -> repository.movieTopRate()
            }
        }
        .cachedIn(viewModelScope)
}
