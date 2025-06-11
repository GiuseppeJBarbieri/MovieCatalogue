package com.vanillacreamsoda.moviecatalogue.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanillacreamsoda.moviecatalogue.data.model.Movie
import com.vanillacreamsoda.moviecatalogue.ui.theme.MovieCatalogueTheme
import coil.compose.AsyncImage
import com.vanillacreamsoda.moviecatalogue.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListCarousel(
    onCardClick: (Long) -> Unit,
    movies: List<Movie>
) {
    val height = 400.dp
    val width = 250.dp
    val posterURL = "https://image.tmdb.org/t/p/w500"
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { movies.count() },
        modifier = Modifier
            .fillMaxWidth()
            .height(height + 100.dp), // Extra padding for title and ratings
        preferredItemWidth = width,
        itemSpacing = 5.dp,
    ) { i ->
        Column {
            val movie = movies[i]
            Card(
                modifier = Modifier
                    .size(width = width, height = height)
                    .maskClip(MaterialTheme.shapes.extraLarge),
                onClick = {
                    onCardClick(movie.id)
                },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AsyncImage(
                        model = posterURL + movie.posterPath,
                        contentDescription = stringResource(R.string.movie_poster),
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(movie.title)
            Row {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(R.string.rating)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(movie.voteAverage.toString())
            }
        }
    }
}

@Preview(device = "id:pixel_9")
@Preview(device = "id:pixel_fold")
@Preview(device = "id:pixel_tablet")
@Composable
fun MovieListCarouselPreview(
) {
    val sampleMovies: List<Movie> = listOf()

    MovieCatalogueTheme {
        MovieListCarousel(
            {},
            movies = sampleMovies
        )
    }
}

