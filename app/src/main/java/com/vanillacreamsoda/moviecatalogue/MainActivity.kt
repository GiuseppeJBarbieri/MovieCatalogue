package com.vanillacreamsoda.moviecatalogue

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vanillacreamsoda.moviecatalogue.ui.views.RootView
import com.vanillacreamsoda.moviecatalogue.ui.theme.MovieCatalogueTheme
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.themoviedb.org/3/trending/movie/day?language=en-US")
            .header("Authorization", "API_KEY")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    Log.i("Giuseppe", response.body!!.string())
                }
            }
        })


        enableEdgeToEdge()
        setContent {
            MovieCatalogueTheme {
                RootView()
            }
        }
    }
}