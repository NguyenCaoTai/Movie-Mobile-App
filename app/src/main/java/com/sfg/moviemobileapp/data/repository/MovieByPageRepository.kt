package com.sfg.moviemobileapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sfg.moviemobileapp.data.api.dto.Movie
import com.sfg.moviemobileapp.data.api.MovieApi
import com.sfg.moviemobileapp.data.api.dto.MovieDetail
import com.sfg.moviemobileapp.data.repository.model.MovieItem
import com.sfg.moviemobileapp.data.repository.model.MovieType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class MovieByPageRepository(private val movieApi: MovieApi) : MovieRepository {

    override fun moviePlaying(): Flow<PagingData<MovieItem>> =
        Pager(
            PagingConfig(MovieApi.PAGE_SIZE),
            1
        ) {
            PageIndexPagingSource(
                movieApi = movieApi,
                MovieType.NowPlaying
            )
        }.flow

    override fun movieTopRate(): Flow<PagingData<MovieItem>> =
        Pager(
            PagingConfig(MovieApi.PAGE_SIZE)
        ) {
            PageIndexPagingSource(
                movieApi = movieApi,
                MovieType.TopRated
            )
        }.flow

    override fun movieDetail(id: String): Flow<MovieDetail> =
        flow {
            emit(movieApi.getMovieDetail(id))
        }
}
