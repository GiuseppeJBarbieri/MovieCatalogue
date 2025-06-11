package com.vanillacreamsoda.moviecatalogue.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableSupportingPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanillacreamsoda.moviecatalogue.R
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

    ParentScaffold(
        movies,
        isLoading,
        errorMessage,
        movieDetails,
        viewModel::fetchMovieDetails,
        viewModel::fetchTrendingMovies
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ParentScaffold(
    movies: List<Movie>,
    isLoading: Boolean,
    errorMessage: String?,
    movieDetails: MovieDetails?,
    fetchMovieDetails: (Long) -> Unit,
    fetchTrendingMovies: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldNavigator = rememberSupportingPaneScaffoldNavigator()
    var movieId by rememberSaveable { mutableLongStateOf(0) }

    // Used for day/week toggle
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(stringResource(R.string.day), stringResource(R.string.week))

    NavigableSupportingPaneScaffold(
        navigator = scaffoldNavigator,
        mainPane = {
            AnimatedPane(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
            ) {
                MainPaneContent(
                    onCardClick = { selectedMovieId ->
                        movieId = selectedMovieId
                        scope.launch {
                            scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                        }
                    },
                    movies = movies,
                    fetchTrendingMovies,
                    selectedIndex,
                    options,
                    onSelectedIndexChange = { newIndex ->
                        selectedIndex = newIndex
                    },
                    isLoading,
                    errorMessage
                )
            }
        },
        supportingPane = {
            AnimatedPane(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount > 50) {
                                scope.launch {
                                    scaffoldNavigator.navigateTo(SupportingPaneScaffoldRole.Main)
                                }
                            }
                        }
                    }
            ) {
                fetchMovieDetails(movieId)
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
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
    movies: List<Movie>,
    fetchTrendingMovies: (String) -> Unit,
    selectedIndex: Int,
    options: List<String>,
    onSelectedIndexChange: (Int) -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.tmdb_movie_header))
        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.most_popular)
            )

            SingleChoiceSegmentedButtonRow {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = {
                            onSelectedIndexChange(index)
                            fetchTrendingMovies(label.lowercase())
                        },
                        selected = index == selectedIndex,
                        label = { Text(label) }
                    )
                }
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
            )
        } else {
            MovieListCarousel(onCardClick, movies)
        }
    }
}

@Preview(device = "id:pixel_9")
@Preview(device = "id:pixel_fold")
@Preview(device = "id:pixel_tablet")
@Composable
fun HomeViewPreview(
) {
    MovieCatalogueTheme {
//        ParentScaffold()
    }
}

