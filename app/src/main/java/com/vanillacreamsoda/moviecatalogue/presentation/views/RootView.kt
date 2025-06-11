package com.vanillacreamsoda.moviecatalogue.presentation.views

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanillacreamsoda.moviecatalogue.presentation.components.navigation.RootNavigationScaffold
import com.vanillacreamsoda.moviecatalogue.presentation.viewModels.RootViewModel

@Composable
fun RootView(
    viewModel: RootViewModel = hiltViewModel(),
) {
    RootNavigationScaffold()
}