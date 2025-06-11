package com.vanillacreamsoda.moviecatalogue.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vanillacreamsoda.moviecatalogue.data.model.Movie
import coil.compose.AsyncImage
import com.vanillacreamsoda.moviecatalogue.R
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListCarousel(
    onCardClick: (Long) -> Unit,
    movies: List<Movie>,
    toggleFavorite: () -> Unit,
    setMovieId: (Long) -> Unit,
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
        Column(
        ) {
            val movie = movies[i]
            Card(
                modifier = Modifier
                    .size(width = width, height = height)
                    .maskClip(MaterialTheme.shapes.extraLarge),
                onClick = {
                    onCardClick(movie.id)
                }
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

                    var isToggled by rememberSaveable { mutableStateOf(false) }
                    IconButton(
                        onClick = {
                            setMovieId(movie.id)
                            isToggled = !isToggled
                            toggleFavorite()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = if (isToggled) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isToggled) stringResource(R.string.favorite) else stringResource(
                                R.string.remove_favorite
                            ),
                            tint = Color.Red
                        )

                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(movie.title)
            Row(
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = stringResource(R.string.rating),
                    tint = Color(0xFFEBB400)
                )
                Spacer(modifier = Modifier.width(5.dp))
                val formattedRating = String.format(Locale.US, "%.1f", movie.voteAverage)
                Text(formattedRating)
            }
        }
    }
}

