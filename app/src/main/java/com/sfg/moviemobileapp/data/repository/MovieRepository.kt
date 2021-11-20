package com.sfg.moviemobileapp.data.repository

import androidx.paging.PagingData
import com.sfg.moviemobileapp.data.api.dto.Movie
import com.sfg.moviemobileapp.data.api.dto.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun moviePlaying(): Flow<PagingData<Movie>>
    fun movieTopRate(): Flow<PagingData<Movie>>
    fun movieDetail(id: String): Flow<MovieDetail>
}