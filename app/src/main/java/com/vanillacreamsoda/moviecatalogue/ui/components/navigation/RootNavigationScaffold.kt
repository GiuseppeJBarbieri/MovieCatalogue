package com.vanillacreamsoda.moviecatalogue.ui.components.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vanillacreamsoda.moviecatalogue.ui.theme.MovieCatalogueTheme
import com.vanillacreamsoda.moviecatalogue.util.MainNavigationBuilder


@Composable
fun RootNavigationScaffold(
) {
    val navController: NavHostController = rememberNavController()
    var currentDestination by rememberSaveable { mutableStateOf(MainNavigationBuilder.MainDestinations.HOME) }
    val colors = MaterialTheme.colorScheme

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteItems = {
            MainNavigationBuilder.MainDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            imageVector =
                                if (currentDestination == it)
                                    it.iconFilled
                                else
                                    it.iconOutlined,
                            contentDescription = stringResource(it.contentDescription),
                            tint = if (currentDestination == it)
                                colors.primary // Selected icon color
                            else
                                colors.onSurface // Unselected icon color
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(it.label),
                            color = if (currentDestination == it)
                                colors.primary // Selected label color
                            else
                                colors.onSurface // Unselected label color
                        )
                    },
                    selected = it == currentDestination,
                    onClick = {
                        currentDestination = it
                        navController.navigate(it.view) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    )
    {
        RootNavigationHost(navController)
    }
}