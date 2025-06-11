package com.vanillacreamsoda.moviecatalogue.network

import com.vanillacreamsoda.moviecatalogue.data.model.MovieDetails
import com.vanillacreamsoda.moviecatalogue.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): MovieResponse

     @GET("movie/{movie_id}")
     suspend fun getMovieDetails(
         @Path("movie_id") movieId: Int,
         @Query("api_key") apiKey: String
     ): MovieDetails
}