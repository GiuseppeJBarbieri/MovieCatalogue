package com.vanillacreamsoda.moviecatalogue.repository

import com.vanillacreamsoda.moviecatalogue.data.model.Movie
import com.vanillacreamsoda.moviecatalogue.network.TMDBApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val tmdbApiService: TMDBApiService
){
    // TODO - Move to buildConfigField
    private val TMDB_API_KEY = "-"

    suspend fun getTrendingMovies(): List<Movie> {
        return tmdbApiService.getTrendingMovies(apiKey = TMDB_API_KEY).results
    }
}