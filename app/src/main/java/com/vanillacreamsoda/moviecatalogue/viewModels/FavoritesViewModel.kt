package com.vanillacreamsoda.moviecatalogue.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanillacreamsoda.moviecatalogue.data.model.MovieDetails
import com.vanillacreamsoda.moviecatalogue.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val movieRepository: MovieRepository
) : ViewModel() {

    // StateFlow for the list of favorite movies
    private val _favoriteMovies = MutableStateFlow<List<MovieDetails>>(emptyList())
    val favoriteMovies: StateFlow<List<MovieDetails>> = _favoriteMovies.asStateFlow()

    // StateFlows for loading and error states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadFavoriteMovies()
    }

    /**
     * Loads favorite movies from the repository and updates the state accordingly.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadFavoriteMovies() {
        viewModelScope.launch {
            // Observe the flow of favorite movie IDs from the repository
            movieRepository.getAllFavoriteMovieIds()
                .onStart {
                    // Emit a loading state when the flow starts
                    _isLoading.value = true
                    _errorMessage.value = null
                }
                .flatMapLatest { favoriteIds ->
                    // When the list of favorite IDs changes
                    if (favoriteIds.isEmpty()) {
                        // If there are no favorite IDs, just emit an empty list immediately
                        _isLoading.value = false
                        flow { emit(emptyList<MovieDetails>()) }
                    } else {
                        // Otherwise, fetch details for each movie ID concurrently.
                        flow {
                            val deferredMovies = favoriteIds.map { id ->
                                viewModelScope.async {
                                    // Launch each fetch concurrently
                                    try {
                                        movieRepository.getMovieDetails(id)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error fetching details for movie ID $id: ${e.message}", Toast.LENGTH_LONG).show()
                                        null
                                    }
                                }
                            }
                            emit(deferredMovies.awaitAll().filterNotNull())
                        }
                    }
                }
                .catch { e ->
                    _errorMessage.value = "Failed to load favorites: ${e.message}"
                    Toast.makeText(context, _errorMessage.value, Toast.LENGTH_LONG).show()
                    _isLoading.value = false
                }
                .collect { loadedMovies ->
                    _favoriteMovies.value = loadedMovies
                    _isLoading.value = false
                    _errorMessage.value = null
                }
        }
    }

    /**
     * Removes favorite movie from list and shared preferences
     */
    fun removeFavorite(movieId: Long) {
        viewModelScope.launch {
            if (movieRepository.getAllFavoriteMovieIds().first().contains(movieId)) {
                movieRepository.removeFavorite(movieId)
            } else {
                movieRepository.addFavorite(movieId)
            }
        }
    }
}