package com.vanillacreamsoda.moviecatalogue.presentation.views

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanillacreamsoda.moviecatalogue.presentation.viewModels.FavoritesViewModel

@Composable
fun FavoritesView(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    Text("Hello Favorites")
}