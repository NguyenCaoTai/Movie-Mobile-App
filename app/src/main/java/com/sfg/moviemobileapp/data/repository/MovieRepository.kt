package com.sfg.moviemobileapp.data.repository

import androidx.paging.PagingData
import com.sfg.moviemobileapp.data.api.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun moviePlaying(): Flow<PagingData<Movie>>
    fun movieTopRate(): Flow<PagingData<Movie>>
}