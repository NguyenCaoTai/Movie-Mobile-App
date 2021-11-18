package com.sfg.moviemobileapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sfg.moviemobileapp.data.api.Movie
import com.sfg.moviemobileapp.data.api.MovieApi
import kotlinx.coroutines.flow.Flow

class MovieByPageRepository(private val movieApi: MovieApi) : MovieRepository {

    override fun moviePlaying(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(MovieApi.PAGE_SIZE),
            1
        ) {
            PageIndexPagingSource(
                movieApi = movieApi,
                MovieApi.MovieType.NowPlaying
            )
        }.flow

    override fun movieTopRate(): Flow<PagingData<Movie>> =
        Pager(
            PagingConfig(MovieApi.PAGE_SIZE)
        ) {
            PageIndexPagingSource(
                movieApi = movieApi,
                MovieApi.MovieType.TopRated
            )
        }.flow
}
