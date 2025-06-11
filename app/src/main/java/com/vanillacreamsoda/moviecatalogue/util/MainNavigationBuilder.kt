package com.vanillacreamsoda.moviecatalogue.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.vanillacreamsoda.moviecatalogue.R
import kotlinx.serialization.Serializable

@Serializable
sealed class MainNavigationBuilder {
    /**
     * Routes
     */
    @Serializable data object Home : MainNavigationBuilder()
    @Serializable data object Favorites : MainNavigationBuilder()
    @Serializable data object Settings : MainNavigationBuilder()

    /**
     * Enum used to build navigation bar items
     */
    enum class MainDestinations(
        @StringRes val label: Int,
        val iconFilled: ImageVector,
        val iconOutlined: ImageVector,
        @StringRes val contentDescription: Int,
        val view: MainNavigationBuilder,
    ) {
        HOME(
            R.string.home_title,
            Icons.Filled.Home,
            Icons.Outlined.Home,
            R.string.home_title,
            Home,
        ),
        FAVORITES(
            R.string.favorites_title,
            Icons.Filled.Favorite,
            Icons.Outlined.FavoriteBorder,
            R.string.favorites_title,
            Favorites,
        ),
        SETTINGS(
            R.string.settings_title,
            Icons.Filled.Settings,
            Icons.Outlined.Settings,
            R.string.settings_title,
            Settings,
        ),
    }
}