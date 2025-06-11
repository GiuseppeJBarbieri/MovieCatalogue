package com.vanillacreamsoda.moviecatalogue.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.vanillacreamsoda.moviecatalogue.R
import com.vanillacreamsoda.moviecatalogue.ui.theme.MovieCatalogueTheme
import com.vanillacreamsoda.moviecatalogue.ui.views.ParentScaffold
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListCarousel(
    onCardClick: (Int) -> Unit
) {
    data class CarouselItem(
        val id: Int,
        @DrawableRes val drawableResId: Int,
        @StringRes val contentDescriptionResId: Int
    )

    val items = listOf(
        CarouselItem(0, 0, R.string.app_name),
        CarouselItem(1, 0, R.string.home_title),
        CarouselItem(2, 0, R.string.details_title),
        CarouselItem(3, 0, R.string.favorites_title),
        CarouselItem(4, 0, R.string.settings_title),
    )

    val height = 200.dp
    val width = 250.dp
    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { items.count() },
        modifier = Modifier
            .fillMaxWidth()
            .height(height + 100.dp),
        preferredItemWidth = width,
        itemSpacing = 5.dp,
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) { i ->
        Column {
            val item = items[i]
            Card(
                modifier = Modifier
                    .size(width = width, height = height)
                    .maskClip(MaterialTheme.shapes.extraLarge),
                onClick = {
                    onCardClick(item.id)
                },
            ) {
                Column {
                    Box(
                    ) {
                        Text(
                            text = stringResource(item.contentDescriptionResId),
                            color = Color.White,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text("Movie Title")
            Row {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating: "
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text("4.5")
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
    MovieCatalogueTheme {
        MovieListCarousel({})
    }
}