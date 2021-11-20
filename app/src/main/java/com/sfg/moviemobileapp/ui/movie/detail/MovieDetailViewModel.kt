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
                        formatMovieDuration(movie.runtime),
                        movie.genres.joinToString(", ") { it.name }
                    ),
                    movie.overview,
                    movie.backdrop_path
                        ?.takeIf { it.isNotEmpty() }
                        ?.let { MOVIE_BASE_IMAGE.format(it) }
                )
            }
            .map { ViewState.Success(it) }
            .catch<ViewState<MovieInfo>> { emit(ViewState.Error(it.message ?: "")) }
            .onStart { emit(ViewState.Loading) }

    private fun formatMovieDuration(runtime: Int): String =
        runtime
            .let {
                MOVIE_DURATION_TEMPLATE.format(
                    it / HOUR_IN_MINUTE,
                    it % HOUR_IN_MINUTE
                )
            }

    companion object {
        const val MOVIE_INFO_FORMAT = "%s/10 | %s | %s"
        const val MOVIE_BASE_IMAGE = "https://image.tmdb.org/t/p/w1280/%s"
        const val MOVIE_TITLE_TEMPLATE = "%s (%s)"
        const val MOVIE_DURATION_TEMPLATE = "%sh %smin"
        const val HOUR_IN_MINUTE = 60
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
