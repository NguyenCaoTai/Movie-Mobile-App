package com.sfg.moviemobileapp.data.api

import android.util.Log
import okhttp3.HttpUrl
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
        @Query("api_key") apiKey: String = "a2c5deb5fdb2ebb10ce53c1fe6b06eca",
    ): ListingResponse

    class ListingResponse(
        val page: Int,
        val results: List<Movie>
    )

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

    enum class MovieType(val type: String){
        NowPlaying("now_playing"),
        TopRated("top_rated")
    }
}