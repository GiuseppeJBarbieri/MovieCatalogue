package com.vanillacreamsoda.moviecatalogue.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import coil.compose.AsyncImage
import com.vanillacreamsoda.moviecatalogue.R
import com.vanillacreamsoda.moviecatalogue.data.model.MovieDetails

@Composable
fun DetailsView(movieDetails: MovieDetails?) {
    val posterUrl = "https://image.tmdb.org/t/p/w500"
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(stringResource(R.string.tmdb_movie_header))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                AsyncImage(
                    model = posterUrl + movieDetails?.posterPath,
                    contentDescription = stringResource(R.string.movie_poster),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )

                var isToggled by rememberSaveable { mutableStateOf(false) }
                IconButton(
                    onClick = { isToggled = !isToggled },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (isToggled) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isToggled) stringResource(R.string.favorite) else stringResource(R.string.remove_favorite),
                        tint = Color.Red
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))

        if (movieDetails != null) {
            Text(movieDetails.title)
            Text(stringResource(R.string.overview))
            Spacer(modifier = Modifier.height(5.dp))
            Text(movieDetails.overview)
            Spacer(modifier = Modifier.height(5.dp))
            Text(movieDetails.releaseDate)
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                movieDetails.genres.forEach { it ->
                    Text(it.name)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(movieDetails.runtime.toString())
            Spacer(modifier = Modifier.height(5.dp))
            Text(movieDetails.revenue.toString())
            Text(stringResource(R.string.director_author))
        }
    }
}
