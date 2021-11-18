package com.sfg.moviemobileapp.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.sfg.moviemobileapp.data.api.Movie
import com.sfg.moviemobileapp.data.api.MovieApi
import java.io.IOException
import retrofit2.HttpException

class PageIndexPagingSource(
    private val movieApi: MovieApi,
    private val movieType: MovieApi.MovieType
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        try {
            movieApi
                .getMovies(
                    type = movieType.type,
                    page = params.key ?: MovieApi.PAGE_INIT
                )
                .let {
                    Page(
                        data = it.results,
                        prevKey = if (params is LoadParams.Prepend) params.key else null,
                        nextKey = it.page + MovieApi.PAGE_STEP,
                    )
                }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
}
