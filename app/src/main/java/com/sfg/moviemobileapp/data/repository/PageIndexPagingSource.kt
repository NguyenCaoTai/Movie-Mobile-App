package com.sfg.moviemobileapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.sfg.moviemobileapp.config.MovieConstant
import com.sfg.moviemobileapp.data.api.dto.Movie
import com.sfg.moviemobileapp.data.api.MovieApi
import com.sfg.moviemobileapp.data.repository.model.MovieItem
import com.sfg.moviemobileapp.data.repository.model.MovieType
import java.io.IOException
import retrofit2.HttpException

class PageIndexPagingSource(
    private val movieApi: MovieApi,
    private val movieType: MovieType
) : PagingSource<Int, MovieItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> =
        try {
            movieApi
                .getMovies(
                    type = movieType.type,
                    page = params.key ?: MovieApi.PAGE_INIT
                )
                .let {
                    Page(
                        data = it.results.map { item ->
                            MovieItem(
                                id = item.id.toString(),
                                title = item.title,
                                releasedYear = item.release_date.split("-")[0],
                                overview = item.overview,
                                voteAverage = item.vote_average.toString(),
                                poster = item.poster_path
                                    .let { String.format(MovieConstant.MOVIE_BASE_THUMB_IMAGE, it) }
                            )
                        },
                        prevKey = if (params is LoadParams.Prepend) params.key else null,
                        nextKey = it.page + MovieApi.PAGE_STEP,
                    )
                }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
}
