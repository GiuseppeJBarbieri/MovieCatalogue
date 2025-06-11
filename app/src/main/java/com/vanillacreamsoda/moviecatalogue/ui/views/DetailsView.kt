package com.vanillacreamsoda.moviecatalogue.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanillacreamsoda.moviecatalogue.R
import com.vanillacreamsoda.moviecatalogue.ui.theme.MovieCatalogueTheme


@Composable
fun DetailsView(id: Long) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("TMDB Movie")
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                var isToggled by rememberSaveable { mutableStateOf(false) }

                IconButton(
                    onClick = { isToggled = !isToggled }
                ) {
                    Icon(
                        imageVector = if (isToggled) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isToggled) "Selected icon button" else "Unselected icon button."
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))

        Text("Movie Title")
        Text("Overview")
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do " +
                    "eiusmod tempor incididunt ut labore et dolore magna aliqua."
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text("PG-13")
        Text("(US)Comedy, Horry, Fantasy")
        Spacer(modifier = Modifier.height(5.dp))
        Text("1h 45m")
        Spacer(modifier = Modifier.height(5.dp))
        Text("Jeremy Sauinier")
        Text("Director, Writer")
    }
}

@Preview(device = "id:pixel_9")
@Preview(device = "id:pixel_fold")
@Preview(device = "id:pixel_tablet")
@Composable
fun DetailsViewPreview(
) {
    MovieCatalogueTheme {
        Surface {
            DetailsView(0)
        }
    }
}
