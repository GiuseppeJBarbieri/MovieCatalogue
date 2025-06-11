package com.vanillacreamsoda.moviecatalogue.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vanillacreamsoda.moviecatalogue.viewModels.FavoritesViewModel
import com.vanillacreamsoda.moviecatalogue.viewModels.HomeViewModel
import com.vanillacreamsoda.moviecatalogue.ui.views.FavoritesView
import com.vanillacreamsoda.moviecatalogue.ui.views.HomeView
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
            HomeView(
                hiltViewModel<HomeViewModel>()
            )
        }

        composable<MainNavigationBuilder.Favorites> {
            FavoritesView(hiltViewModel<FavoritesViewModel>())
        }
    }
}