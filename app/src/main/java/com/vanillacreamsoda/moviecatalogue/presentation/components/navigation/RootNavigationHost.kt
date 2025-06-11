package com.vanillacreamsoda.moviecatalogue.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vanillacreamsoda.moviecatalogue.presentation.viewModels.DetailsViewModel
import com.vanillacreamsoda.moviecatalogue.presentation.viewModels.FavoritesViewModel
import com.vanillacreamsoda.moviecatalogue.presentation.viewModels.HomeViewModel
import com.vanillacreamsoda.moviecatalogue.presentation.views.DetailsView
import com.vanillacreamsoda.moviecatalogue.presentation.views.FavoritesView
import com.vanillacreamsoda.moviecatalogue.presentation.views.HomeView
import com.vanillacreamsoda.moviecatalogue.util.MainNavigationBuilder


@Composable
fun RootNavigationHost(
    navController: NavHostController
) {
    NavHost(
        navController,
        startDestination = MainNavigationBuilder.Home
    ) {
        composable<MainNavigationBuilder.Home> {
            HomeView(hiltViewModel<HomeViewModel>())
        }
        composable<MainNavigationBuilder.Details> {
            DetailsView(hiltViewModel<DetailsViewModel>())
        }
        composable<MainNavigationBuilder.Favorites> {
            FavoritesView(hiltViewModel<FavoritesViewModel>())
        }
    }
}