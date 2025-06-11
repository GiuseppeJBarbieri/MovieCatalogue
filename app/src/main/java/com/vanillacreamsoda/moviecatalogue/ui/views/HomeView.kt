package com.vanillacreamsoda.moviecatalogue.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableSupportingPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanillacreamsoda.moviecatalogue.data.model.Movie
import com.vanillacreamsoda.moviecatalogue.data.model.MovieDetails
import com.vanillacreamsoda.moviecatalogue.ui.components.MovieListCarousel
import com.vanillacreamsoda.moviecatalogue.viewModels.HomeViewModel
import com.vanillacreamsoda.moviecatalogue.ui.theme.MovieCatalogueTheme
import kotlinx.coroutines.launch

@Composable
fun HomeView(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val movies by viewModel.trendingMovies.collectAsState()
    val movieDetails by viewModel.movieDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    ParentScaffold(movies, isLoading, errorMessage, movieDetails, viewModel::fetchMovieDetails)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ParentScaffold(
    movies: List<Movie>,
    isLoading: Boolean,
    errorMessage: String?,
    movieDetails: MovieDetails?,
    fetchMovieDetails: (Long) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldNavigator = rememberSupportingPaneScaffoldNavigator()
    var movieId by rememberSaveable { mutableLongStateOf(0) }

    NavigableSupportingPaneScaffold(
        navigator = scaffoldNavigator,
        mainPane = {
            AnimatedPane(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
            ) {
                if (isLoading) {
//                    CircularProgressIndicator()
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                    )
                } else {
                    MainPaneContent(
                        onCardClick = { selectedMovieId ->
                            movieId = selectedMovieId
                            scope.launch {
                                scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                            }
                        },
                        movies = movies
                    )
                }
            }
        },
        supportingPane = {
            AnimatedPane(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            if (dragAmount > 50) { // Swipe right to navigate back
                                scope.launch {
                                    scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Main)
                                }
                            }
                        }
                    }
            ) {
                fetchMovieDetails(movieId)
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                    )
                } else {
                    DetailsView(movieDetails)
                }
            }
        }
    )
}

@Composable
private fun MainPaneContent(
    onCardClick: (Long) -> Unit,
    movies: List<Movie>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text("TMDB Movie")
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = "Most Popular"
            )
            OutlinedButton(
                onClick = { },
            ) {
                Text("Today")
            }
            OutlinedButton(
                onClick = { },
            ) {
                Text("This Week")
            }
        }
        MovieListCarousel(onCardClick, movies)
    }
}

@Preview(device = "id:pixel_9")
@Preview(device = "id:pixel_fold")
@Preview(device = "id:pixel_tablet")
@Composable
fun HomeViewPreview(
) {
    MovieCatalogueTheme {
//        ParentScaffold(
//
//        )
    }
}

