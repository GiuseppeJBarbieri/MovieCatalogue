package com.vanillacreamsoda.moviecatalogue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vanillacreamsoda.moviecatalogue.ui.views.RootView
import com.vanillacreamsoda.moviecatalogue.ui.theme.MovieCatalogueTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieCatalogueTheme {
                RootView()
            }
        }
    }
}