package com.sfg.moviemobileapp.data.api

import android.util.Log
import com.sfg.moviemobileapp.BuildConfig
import com.sfg.moviemobileapp.data.api.dto.ListingResponse
import com.sfg.moviemobileapp.data.api.dto.Movie
import com.sfg.moviemobileapp.data.api.dto.MovieDetail
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/{type}")
    suspend fun getMovies(
        @Path("type") type: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    ): ListingResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") id: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): MovieDetail

    companion object {
        const val PAGE_INIT = 1
        const val PAGE_STEP = 1
        const val PAGE_SIZE = 20
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create(): MovieApi {
            val logger = HttpLoggingInterceptor { Log.d("API", it) }
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrlOrNull()!!)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApi::class.java)
        }
    }
}