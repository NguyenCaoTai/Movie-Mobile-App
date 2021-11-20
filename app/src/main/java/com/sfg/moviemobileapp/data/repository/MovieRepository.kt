package com.sfg.moviemobileapp.data.repository

import androidx.paging.PagingData
import com.sfg.moviemobileapp.data.api.dto.Movie
import com.sfg.moviemobileapp.data.api.dto.MovieDetail
import com.sfg.moviemobileapp.data.repository.model.MovieItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun moviePlaying(): Flow<PagingData<MovieItem>>
    fun movieTopRate(): Flow<PagingData<MovieItem>>
    fun movieDetail(id: String): Flow<MovieDetail>
}