package com.what3words.search.wrapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.what3words.androidwrapper.datasource.text.W3WApiTextDataSource
import com.what3words.search.wrapper.bng.BritishNationalGridSearch
import com.what3words.search.wrapper.coordinates.CoordinatesSearch
import com.what3words.search.wrapper.core.W3WSearchClient
import com.what3words.search.wrapper.googleplaces.GooglePlacesConfig
import com.what3words.search.wrapper.googleplaces.GooglePlacesSearch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val textDataSource = W3WApiTextDataSource.create(this, BuildConfig.W3W_WRAPPER_API_KEY)
        val searchClient = W3WSearchClient(textDataSource) {
            install(BritishNationalGridSearch, priority = 10)
            install(CoordinatesSearch, priority = 9)
            install(
                plugin = GooglePlacesSearch,
                config = GooglePlacesConfig(context = this@MainActivity, apiKey = BuildConfig.PLACES_API),
                priority = 1,
            )
        }

        setContent {
            App(
                viewModel = viewModel<SearchViewModel>(
                    factory = SearchViewModel.Factory(
                        searchClient
                    )
                )
            )
        }
    }
}
