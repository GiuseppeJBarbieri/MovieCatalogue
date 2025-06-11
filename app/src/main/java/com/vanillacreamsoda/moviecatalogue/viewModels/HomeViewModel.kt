package com.vanillacreamsoda.moviecatalogue.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanillacreamsoda.moviecatalogue.data.model.Movie
import com.vanillacreamsoda.moviecatalogue.data.model.MovieDetails
import com.vanillacreamsoda.moviecatalogue.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val trendingMovies: StateFlow<List<Movie>> = _trendingMovies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _movieDetails = MutableStateFlow<MovieDetails?>(null)
    val movieDetails: StateFlow<MovieDetails?> = _movieDetails.asStateFlow()

    init {
        fetchTrendingMovies()
    }

    /**
     * Fetch trending movie list from TMDB API
     * @param timeWindow - time frame for most trending (day,week)
     */
    fun fetchTrendingMovies(timeWindow: String = "day") {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val movies = movieRepository.getTrendingMovies(timeWindow)
                _trendingMovies.value = movies
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch movies: ${e.localizedMessage}"
                _trendingMovies.value = emptyList()
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fetch Movie Details based on movieID
     * @param movieId - ID of Movie
     */
    fun fetchMovieDetails(movieId: Long) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val movieDetails = movieRepository.getMovieDetails(movieId)
                _movieDetails.value = movieDetails
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch movies: ${e.localizedMessage}"
                _movieDetails.value = null
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

}