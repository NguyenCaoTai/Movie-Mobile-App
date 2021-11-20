package com.sfg.moviemobileapp.ui.movie.detail

import androidx.lifecycle.ViewModel
import com.sfg.moviemobileapp.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onErrorCollect
import kotlinx.coroutines.flow.onStart

class MovieDetailViewModel(
    val movieId: String,
    val repository: MovieRepository
) : ViewModel() {
    val movieDetail: Flow<ViewState<MovieInfo>> =
        repository
            .movieDetail(movieId)
            .map { movie ->
                MovieInfo(
                    String.format(
                        MOVIE_TITLE_TEMPLATE,
                        movie.title,
                        movie.release_date.split("-")[0]
                    ),
                    String.format(
                        MOVIE_INFO_FORMAT,
                        movie.vote_average,
                        movie.runtime,
                        movie.genres.map { it.name }.joinToString(", ")
                    ),
                    movie.overview,
                    movie.backdrop_path
                        ?.takeIf { it.isNotEmpty() }
                        ?.let { String.format(MOVIE_BASE_IMAGE, it) }
                )
            }
            .map { ViewState.Success(it) }
            .catch { ViewState.Error(it.message ?: "") }
            .onStart { ViewState.Loading }

    companion object {
        const val MOVIE_INFO_FORMAT = "%s/10 | %s | %s"
        const val MOVIE_BASE_IMAGE = "https://image.tmdb.org/t/p/w1280/%s"
        const val MOVIE_TITLE_TEMPLATE = "%s (%s)"
    }
}

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Error(val info: String) : ViewState<Nothing>()
    data class Success<T>(val payload: T) : ViewState<T>()
}

data class MovieInfo(
    val title: String,
    val mixedInfo: String,
    val overview: String,
    val backdrop: String?
)
