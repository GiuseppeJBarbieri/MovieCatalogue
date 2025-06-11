package com.vanillacreamsoda.moviecatalogue.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
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
import com.vanillacreamsoda.moviecatalogue.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class MovieRepository @Inject constructor(
    private val tmdbApiService: TMDBApiService,
    @ApplicationContext private val appContext: Context

) {
    private val tmdbAPIKey = BuildConfig.TMDB_API_KEY

    /**
     * Shared Preferences
     */
    private val movieCachePrefsName = "movie_cache_prefs"
    private val keyTrendingMoviesDay = "trending_movies_day_json"
    private val keyTrendingMoviesWeek = "trending_movies_week_json"
    private val keyCacheTimestampDay = "trending_movies_cache_timestamp_day"
    private val keyCacheTimestampWeek = "trending_movies_cache_timestamp_week"
    private val keyFavoriteIds = "favorite_movie_ids"

    private val cacheDurationMillis = 60 * 60 * 1000L // 1 hour Cache Time
    private val artificialDelay = 750L
    private val gson = Gson()

    private val _favoriteMovieIdsFlow: MutableStateFlow<List<Long>> = MutableStateFlow(loadFavoriteMovieIdsFromPrefs())
    private val favoriteMovieIdsFlow: StateFlow<List<Long>> = _favoriteMovieIdsFlow.asStateFlow()

    private val sharedPreferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(movieCachePrefsName, Context.MODE_PRIVATE)
    }

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
                    apiKey = tmdbAPIKey,
                    timeWindow = timeWindow
                ).results

                saveMoviesToCache(networkMovies, timeWindow)
                return networkMovies
            } catch (e: Exception) {
                if (cachedMovies != null) {
                    // If network call fails, and there's some stale cache, return that.
                    return cachedMovies
                } else {
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
            else -> keyTrendingMoviesDay to keyCacheTimestampDay
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
            apiKey = tmdbAPIKey,
            movieId = movieId.toInt()
        )
    }

    /**
     * Read the JSON string and convert it to a List<Long>
     */
    private fun loadFavoriteMovieIdsFromPrefs(): List<Long> {
        val json = sharedPreferences.getString(keyFavoriteIds, null)
        return if (json == null) {
            emptyList()
        } else {
            Log.d("Giuseppe", "Add Favorite: $json")
            val type = object : TypeToken<List<Long>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        }
    }

    /**
     * Convert the List<Long> to JSON and save it
     */
    private fun saveFavoriteMovieIdsToPrefs(ids: List<Long>) {
        val json = gson.toJson(ids)
        sharedPreferences.edit { putString(keyFavoriteIds, json) }
        _favoriteMovieIdsFlow.value = ids // Update the Flow
        Log.d("Giuseppe", "Saved favorite movie IDs: $ids")
    }

    /**
     * Adds movieId to the list of favorite movies
     */
    suspend fun addFavorite(movieId: Long) = withContext(Dispatchers.IO) {
        val currentFavorites = loadFavoriteMovieIdsFromPrefs().toMutableList()
        if (movieId !in currentFavorites) {
            currentFavorites.add(movieId)
            saveFavoriteMovieIdsToPrefs(currentFavorites)
            Log.d("Giuseppe", "Add Favorite: $currentFavorites")
            Log.d("Giuseppe", "Add Favorite: $movieId")
        }
    }

    /**
     * Removes movieId from the list of favorite movies
     */
    suspend fun removeFavorite(movieId: Long) = withContext(Dispatchers.IO) {
        val currentFavorites = loadFavoriteMovieIdsFromPrefs().toMutableList()
        if (movieId in currentFavorites) {
            currentFavorites.remove(movieId)
            saveFavoriteMovieIdsToPrefs(currentFavorites)
        }
    }

    /**
     * Checks if movieId is in the list of favorite movies
     */
    fun isFavorite(movieId: Long): Flow<Boolean> {
        // Observe the flow of favorite IDs and map it to check existence
        return _favoriteMovieIdsFlow.map { it.contains(movieId) }
    }

    /**
     * Returns the flow of favorite movie IDs
     */
    fun getAllFavoriteMovieIds(): Flow<List<Long>> {
        return favoriteMovieIdsFlow
    }
}