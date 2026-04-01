package com.what3words.search.wrapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.what3words.androidwrapper.datasource.text.W3WApiTextDataSource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val textDataSource = W3WApiTextDataSource.create(this, BuildConfig.W3W_WRAPPER_API_KEY)

        setContent {
            App(
                viewModel = viewModel<SearchViewModel>(
                    factory = SearchViewModel.Factory(
                        textDataSource = textDataSource,
                    )
                )
            )
        }
    }
}
