package com.what3words.search.wrapper

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.W3WSearchClient
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
}

data class UiState(
    val query: String = "",
    val suggestions: List<SearchResult> = emptyList(),
    val resolvedAddress: SearchResult.ResolvedAddress? = null,
    val isSearching: Boolean = false,
    val isResolving: Boolean = false,
    val error: String? = null,
)

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchClient: W3WSearchClient,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var searchTask: Job? = null

    init {
        viewModelScope.launch {
            _uiState
                .map { it.query }
                .filter { it.isNotBlank() }
                .debounce(300L)
                .distinctUntilChanged()
                .collect { q ->
                    performSearch(q)
                }
        }
    }

    fun handleAction(action: SearchAction) {
        when (action) {
            is SearchAction.QueryChanged -> onQueryChange(action.query)
            is SearchAction.SuggestionSelected -> onSuggestionSelected(action.suggestion)
            SearchAction.ClearQuery -> onQueryCleared()
        }
    }

    private fun onQueryChange(newQuery: String) {
        _uiState.update {
            it.copy(
                query = newQuery,
                resolvedAddress = null,
                error = null,
                isSearching = false,
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
            when (val result = searchClient.resolve(suggestion)) {
                is W3WResult.Success ->
                    setSelectedAddress(result.value)

                is W3WResult.Failure ->
                    _uiState.update {
                        it.copy(error = result.error.message ?: "Failed to resolve address")
                    }
            }
            _uiState.update { it.copy(isResolving = false) }
        }
    }

    private fun setSelectedAddress(address: SearchResult.ResolvedAddress) {
        Log.d("SearchViewModel", "Selected address: $address")
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    resolvedAddress = address,
                    suggestions = emptyList(),
                    query = ""
                )
            }
        }
    }

    private fun performSearch(query: String) {
        _uiState.update { it.copy(isSearching = true, error = null) }

        searchTask?.cancel()
        searchTask = viewModelScope.launch {
            when (val result = searchClient.search(query)) {
                is W3WResult.Success ->
                    _uiState.update {
                        it.copy(
                            suggestions = result.value,
                            isSearching = false,
                        )
                    }

                is W3WResult.Failure ->
                    _uiState.update {
                        it.copy(
                            suggestions = emptyList(),
                            error = result.error.message ?: "Search failed",
                            isSearching = false,
                        )
                    }
            }
        }
    }

    class Factory(
        private val searchClient: W3WSearchClient,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SearchViewModel(searchClient) as T
    }
}
