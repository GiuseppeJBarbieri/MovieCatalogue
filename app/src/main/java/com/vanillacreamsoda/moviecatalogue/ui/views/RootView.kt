package com.vanillacreamsoda.moviecatalogue.ui.views

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanillacreamsoda.moviecatalogue.ui.components.navigation.RootNavigationScaffold
import com.vanillacreamsoda.moviecatalogue.viewModels.RootViewModel

@Composable
fun RootView(
    viewModel: RootViewModel = hiltViewModel(),
) {
    RootNavigationScaffold()
}