package com.vanillacreamsoda.moviecatalogue.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vanillacreamsoda.moviecatalogue.data.model.Movie
import com.vanillacreamsoda.moviecatalogue.data.model.MovieDetails
import com.vanillacreamsoda.moviecatalogue.network.TMDBApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class MovieRepository @Inject constructor(
    private val tmdbApiService: TMDBApiService,
    @ApplicationContext private val appContext: Context

) {
    // TODO - Move to buildConfigField
    private val TMDB_API_KEY = "-"

    /**
     * Shared Preferences
     */
    private val PREFS_NAME = "movie_cache_prefs"
    private val keyTrendingMoviesDay = "trending_movies_day_json"
    private val keyTrendingMoviesWeek = "trending_movies_week_json"
    private val keyCacheTimestampDay = "trending_movies_cache_timestamp_day"
    private val keyCacheTimestampWeek = "trending_movies_cache_timestamp_week"
    private val cacheDurationMillis = 60 * 60 * 1000L // 1 hour Cache Time
    private val artificialDelay = 750L

    private val sharedPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val gson = Gson()

    /**
     * Checks to see if movie list is already saved to cache for the selected time window (i.e "day, "week").
     * If not cached it will request the movie list from the tmdb api
     */
    suspend fun getTrendingMovies(timeWindow: String = "day"): List<Movie> {
        val (cachedMovies, cacheTimestampKey) = when (timeWindow) {
            "day" -> getMoviesFromCache(keyTrendingMoviesDay) to keyCacheTimestampDay
            "week" -> getMoviesFromCache(keyTrendingMoviesWeek) to keyCacheTimestampWeek
            else -> getMoviesFromCache(keyTrendingMoviesDay) to keyCacheTimestampDay
        }

        val cacheTimestamp = sharedPreferences.getLong(cacheTimestampKey, 0L)
        val isCacheValid =
            cachedMovies != null && (System.currentTimeMillis() - cacheTimestamp < cacheDurationMillis)

        if (isCacheValid) {
            // If cache is valid, return cached data immediately
            return cachedMovies!!
        } else {
            // If cache is invalid or empty, fetch from network
            try {
                // 750ms delay so spinner is able to be seen during page load
                delay(artificialDelay)
                val networkMovies = tmdbApiService.getTrendingMovies(
                    language = "en-US",
                    apiKey = _tmdbAPIKey,
                    timeWindow = timeWindow
                ).results

                // Save new data to cache
                saveMoviesToCache(networkMovies, timeWindow)

                return networkMovies
            } catch (e: Exception) {
                if (cachedMovies != null) {
                    // If network call fails, and there's some stale cache, return that.
                    return cachedMovies
                } else {
                    // Otherwise, re-throw the exception.
                    throw e
                }
            }
        }
    }

    /**
     * Saves trending movie list to cache for both week and day
     */
    private fun saveMoviesToCache(movies: List<Movie>, timeWindow: String) {
        val (key, timestampKey) = when (timeWindow) {
            "day" -> keyTrendingMoviesDay to keyCacheTimestampDay
            "week" -> keyTrendingMoviesWeek to keyCacheTimestampWeek
            else -> keyTrendingMoviesDay to keyCacheTimestampDay // Default to day
        }
        val moviesJson = gson.toJson(movies)
        sharedPreferences.edit {
            putString(key, moviesJson)
                .putLong(timestampKey, System.currentTimeMillis())
        }
    }

    /**
     * Gets trending movie list from cache based on provided key
     */
    private fun getMoviesFromCache(key: String): List<Movie>? {
        val moviesJson = sharedPreferences.getString(key, null)
        return if (moviesJson != null) {
            val type = object : TypeToken<List<Movie>>() {}.type
            gson.fromJson(moviesJson, type)
        } else {
            null
        }
    }

    /**
     * Calls TMDB API service to request movie details by movieId
     */
    suspend fun getMovieDetails(movieId: Long): MovieDetails {
        // 750ms delay so spinner is able to be seen during page load
        delay(artificialDelay)
        return tmdbApiService.getMovieDetails(
            apiKey = _tmdbAPIKey,
            movieId = movieId.toInt()
        )
    }
}