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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
        Text(stringResource(R.string.tmdb_movie_header), style = MaterialTheme.typography.headlineMedium)
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .height(450.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                AsyncImage(
                    model = posterUrl + movieDetails?.posterPath,
                    contentDescription = stringResource(R.string.movie_poster),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxWidth()
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
                        contentDescription = if (isToggled) stringResource(R.string.favorite) else stringResource(
                            R.string.remove_favorite
                        ),
                        tint = Color.Red
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        if (movieDetails != null) {
            Text(text = movieDetails.title, style = MaterialTheme.typography.headlineMedium)
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Text(
                text = stringResource(R.string.overview)
            )
            Text(movieDetails.overview)
            Spacer(modifier = Modifier.height(5.dp))
            Text(if (movieDetails.adult) stringResource(R.string.adult) else stringResource(R.string.not_adult))
            Text(movieDetails.releaseDate)
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text("(" + movieDetails.productionCompanies[0].name + ")")
                movieDetails.genres.forEach { it ->
                    Text(it.name)
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(convertMinutesToHhMmFormat(movieDetails.runtime))
            Spacer(modifier = Modifier.height(5.dp))
            Text(movieDetails.title)
            Text(stringResource(R.string.director_author))
        }
    }
}

/**
 * Converts a total number of minutes into a human-readable "Hhr MMm" format.
 *
 * @param totalMinutes The total duration in minutes (Long).
 * @return A string representing the duration, e.g., "1hr 45m", "2hr", "30m", or "0m".
 */
fun convertMinutesToHhMmFormat(totalMinutes: Long): String {
    if (totalMinutes < 0) {
        return "Invalid Duration"
    }

    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    val hoursPart = if (hours > 0) "${hours}hr" else ""
    val minutesPart = if (minutes > 0) "${minutes}m" else ""

    return when {
        hours > 0 && minutes > 0 -> "$hoursPart $minutesPart" // e.g., "1hr 45m"
        hours > 0 && minutes == 0L -> hoursPart // e.g., "2hr"
        hours == 0L && minutes > 0 -> minutesPart // e.g., "30m"
        else -> "0m" // When totalMinutes is 0
    }
}