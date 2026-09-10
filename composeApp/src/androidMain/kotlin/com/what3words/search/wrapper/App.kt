package com.what3words.search.wrapper

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.what3words.core.types.domain.W3WAddress
import com.what3words.core.types.domain.W3WCountry
import com.what3words.core.types.language.W3WProprietaryLanguage
import com.what3words.design.library.ui.components.What3wordsAddressListItem
import com.what3words.design.library.ui.models.DisplayUnits
import com.what3words.design.library.ui.models.formatDistance
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_DISTANCE_TO_FOCUS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUBTITLE
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_TITLE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: SearchViewModel) {
    MaterialTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Address Search", fontWeight = FontWeight.SemiBold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    actions = {
                        TextButton(
                            enabled = uiState.mapSwitcherEnabled,
                            onClick = {
                                viewModel.handleAction(SearchAction.SwitchMapProvider)
                            }
                        ) {
                            Text(
                                when (uiState.mapProvider) {
                                    MapProvider.Mapbox -> "Mapbox"
                                    MapProvider.Google -> "Google Maps"
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
            ) {
                // ── Search field ──────────────────────────────────────────────
                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = { viewModel.handleAction(SearchAction.QueryChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search for a place…") },
                    leadingIcon = {
                        Text(
                            text = "🔍",
                            modifier = Modifier.padding(start = 4.dp),
                        )
                    },
                    trailingIcon = {
                        if (uiState.query.isNotEmpty()) {
                            IconButton(onClick = { viewModel.handleAction(SearchAction.ClearQuery) }) {
                                Text(text = "✕", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                )

                // ── Clip-to-VN toggle ─────────────────────────────────────────
                // Demonstrates mutating ThreeWordAddressSearchConfig.clippedCountries
                // at runtime without rebuilding the W3WSearchClient.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = "Clip 3wa to VN",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f),
                    )
                    Switch(
                        checked = uiState.clipToVn,
                        onCheckedChange = {
                            viewModel.handleAction(SearchAction.SetClipToVn(it))
                        },
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // ── Search progress ───────────────────────────────────────────
                if (uiState.isSearching) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                // ── Error banner ──────────────────────────────────────────────
                uiState.error?.let { errorMsg ->
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    )
                }

                // ── Resolved address card ─────────────────────────────────────
                uiState.resolvedAddress?.let { resolved ->
                    What3wordsAddressListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        words = resolved.address.words,
                        nearestPlace = resolved.address.nearestPlace,
                        isLand = true,
                        distanceMeters = resolved.extras[EXTRAS_KEY_DISTANCE_TO_FOCUS]?.toIntOrNull(),
                        displayUnits = DisplayUnits.METRIC,
                        isHighlighted = false,
                    )
                }

                // ── Suggestions list or resolve spinner ───────────────────────
                if (uiState.isResolving) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {
                        uiState.suggestions.groupBy { it.providerId }
                            .map { (providerId, suggestions) ->
                                stickyHeader {
                                    Text(
                                        providerId,
                                        style = MaterialTheme.typography.titleSmall,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                                items(suggestions, key = { it.hashCode() }) { suggestion ->
                                    SearchResultItem(
                                        suggestion = suggestion,
                                        onClick = {
                                            viewModel.handleAction(
                                                SearchAction.SuggestionSelected(
                                                    it
                                                )
                                            )
                                        },
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                                }
                            }
                    }

                }

                uiState.didYouMean?.let { suggestion ->
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Did you mean...", modifier = Modifier.padding(horizontal = 16.dp))
                    Text(
                        text = suggestion,
                        modifier = Modifier
                            .clickable {
                                viewModel.handleAction(SearchAction.QueryChanged(suggestion))
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                if (uiState.didYouMean == null && uiState.suggestions.isEmpty() && !uiState.query.isEmpty()) {
                    Text("No address found", modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun SearchResultItem(
    suggestion: SearchResult,
    onClick: (SearchResult) -> Unit,
) {
    val title = suggestion.extras[EXTRAS_KEY_TITLE].orEmpty()
    val subtitle = suggestion.extras[EXTRAS_KEY_SUBTITLE].orEmpty()
    val distanceToFocus = suggestion.extras[EXTRAS_KEY_DISTANCE_TO_FOCUS].orEmpty()
    val compositionLocale = LocalConfiguration.current.locales[0]

    when (suggestion) {
        is SearchResult.SearchSuggestion -> ListItem(
            headlineContent = { Text(title) },
            supportingContent = {
                Column {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall)
                    distanceToFocus.toIntOrNull()?.let { meters ->
                        Text(
                            // Pass the composition locale so this hand-rolled row formats the
                            // same way What3wordsAddressListItem does internally.
                            formatDistance(meters, DisplayUnits.METRIC, locale = compositionLocale),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            leadingContent = {
                Text(
                    text = "📍",
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            modifier = Modifier.clickable { onClick(suggestion) },
        )

        is SearchResult.ResolvedAddress -> What3wordsAddressListItem(
            words = suggestion.address.words,
            nearestPlace = suggestion.address.nearestPlace,
            isLand = true,
            distanceMeters = distanceToFocus.toIntOrNull(),
            displayUnits = DisplayUnits.METRIC,
            isHighlighted = false,
            showDivider = false
        )
    }

}

@Preview
@Composable
private fun PreviewSearchResultItem() {
    MaterialTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SearchResultItem(
                suggestion = SearchResult.SearchSuggestion(
                    query = "Sonatus",
                    providerId = "google_places",
                    extras = mapOf("title" to "Sonatus Building", "subtitle" to "Saigon, Vietnam"),
                ),
                onClick = {},
            )

            SearchResultItem(
                suggestion = SearchResult.ResolvedAddress(
                    "filled.count.soap",
                    "w3w",
                    W3WAddress(
                        words = "filled.count.soap",
                        center = null,
                        square = null,
                        language = W3WProprietaryLanguage("en", null, null, null),
                        country = W3WCountry("GB"),
                        nearestPlace = "London"
                    )
                ),
                onClick = {},
            )
        }
    }
}
