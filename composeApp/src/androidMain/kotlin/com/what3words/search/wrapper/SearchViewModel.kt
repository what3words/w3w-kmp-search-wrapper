package com.what3words.search.wrapper

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.core.SearchResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class SearchAction {
    data class QueryChanged(val query: String) : SearchAction()
    data class SuggestionSelected(val suggestion: SearchResult) : SearchAction()
    data object ClearQuery : SearchAction()
    data object SwitchMapProvider : SearchAction()
}

data class UiState(
    val query: String = "",
    val mapProvider: MapProvider = MapProvider.Google,
    val mapSwitcherEnabled: Boolean = true,
    val suggestions: List<SearchResult> = emptyList(),
    val resolvedAddress: SearchResult.ResolvedAddress? = null,
    val isSearching: Boolean = false,
    val isResolving: Boolean = false,
    val didYouMean: String? = null,
    val error: String? = null,
)

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchClientProvider: SearchClientProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var searchTask: Job? = null

    init {
        // Re-run search whenever the query or active provider changes.
        viewModelScope.launch {
            _uiState
                .map { it.query to it.mapProvider }
                .filter { (query, _) -> query.isNotBlank() }
                .debounce(300L)
                .distinctUntilChanged()
                .collect { (query, _) ->
                    performSearch(query)
                }
        }
    }

    fun handleAction(action: SearchAction) {
        when (action) {
            is SearchAction.QueryChanged -> onQueryChange(action.query)
            is SearchAction.SuggestionSelected -> onSuggestionSelected(action.suggestion)
            is SearchAction.ClearQuery -> onQueryCleared()
            is SearchAction.SwitchMapProvider -> switchMapProvider()
        }
    }

    private fun switchMapProvider() {
        _uiState.update {
            val next = if (it.mapProvider == MapProvider.Google) MapProvider.Mapbox else MapProvider.Google
            it.copy(mapProvider = next, query = "", suggestions = emptyList())
        }
    }

    private fun onQueryChange(newQuery: String) {
        _uiState.update {
            it.copy(
                query = newQuery,
                resolvedAddress = null,
                error = null,
                isSearching = false,
                didYouMean = null,
                suggestions = if (newQuery.isBlank()) emptyList() else it.suggestions,
            )
        }
    }

    private fun onQueryCleared() {
        searchTask?.cancel()
        onQueryChange("")
    }

    private fun onSuggestionSelected(suggestion: SearchResult) {
        when (suggestion) {
            is SearchResult.SearchSuggestion -> resolveAddress(suggestion)
            is SearchResult.ResolvedAddress -> setSelectedAddress(suggestion)
        }
    }

    private fun resolveAddress(suggestion: SearchResult.SearchSuggestion) {
        viewModelScope.launch {
            _uiState.update { it.copy(isResolving = true, error = null, resolvedAddress = null) }
            when (val result = searchClientProvider.clientFor(_uiState.value.mapProvider).resolve(suggestion)) {
                is W3WResult.Success -> {
                    checkChineseAddressAndSwitchMap(result.value)
                    setSelectedAddress(result.value)
                }
                is W3WResult.Failure ->
                    _uiState.update {
                        it.copy(error = result.error.message ?: result.message ?: "Failed to resolve address")
                    }
            }
            _uiState.update { it.copy(isResolving = false) }
        }
    }

    private fun checkChineseAddressAndSwitchMap(address: SearchResult.ResolvedAddress) {
        // Google Maps is required for China (CN) — Mapbox is not available there.
        if (address.address.country.twoLetterCode == "CN" && _uiState.value.mapProvider == MapProvider.Mapbox) {
            setMapSwitcherEnabled(false)
            switchMapProvider()
        } else {
            setMapSwitcherEnabled(true)
        }
    }

    private fun setMapSwitcherEnabled(enabled: Boolean) {
        _uiState.update { it.copy(mapSwitcherEnabled = enabled) }
    }

    private fun setSelectedAddress(address: SearchResult.ResolvedAddress) {
        Log.d("SearchViewModel", "Selected address: $address")
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    resolvedAddress = address,
                    suggestions = emptyList(),
                    didYouMean = null,
                    query = ""
                )
            }
        }
    }

    private fun performSearch(query: String) {
        _uiState.update { it.copy(isSearching = true, error = null) }

        searchTask?.cancel()
        searchTask = viewModelScope.launch {
            val client = searchClientProvider.clientFor(_uiState.value.mapProvider)
            when (val result = client.search(query)) {
                is W3WResult.Success -> {
                    val didYouMean = result.value
                        .firstOrNull { (it as? SearchResult.SearchSuggestion)?.suggestedAddressOrNull() != null }

                    val suggestions = result.value.filter {
                        it != didYouMean
                    }

                    _uiState.update {
                        it.copy(
                            suggestions = suggestions,
                            didYouMean = (didYouMean as? SearchResult.SearchSuggestion)?.suggestedAddressOrNull(),
                            isSearching = false
                        )
                    }
                }
                is W3WResult.Failure ->
                    _uiState.update {
                        it.copy(
                            suggestions = emptyList(),
                            didYouMean = null,
                            error = result.error.message ?: "Search failed",
                            isSearching = false,
                        )
                    }
            }
        }
    }

    class Factory(
        private val searchClientProvider: SearchClientProvider,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SearchViewModel(searchClientProvider) as T
    }
}
