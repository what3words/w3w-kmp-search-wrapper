# what3words KMP Search Wrapper

[![Maven Central](https://img.shields.io/maven-central/v/com.what3words/w3w-kmp-search-wrapper.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/com.what3words/w3w-kmp-search-wrapper)
![Stable](https://img.shields.io/github/v/release/what3words/w3w-kmp-search-wrapper)
![Pre-release](https://img.shields.io/github/v/release/what3words/w3w-kmp-search-wrapper?include_prereleases&label=pre-release)
![Platforms](https://img.shields.io/badge/platforms-Android%20%7C%20iOS-blue)

Kotlin Multiplatform search client for Android and iOS. Combines what3words address resolution with third-party geocoders (Mapbox, Google Places, British National Grid, plain coordinates) under one API.

## Features

- One `W3WSearchClient` API. Install plugins, then call `search(query)` and `resolve(suggestion)`.
- Priority tiers run capable providers concurrently and merge their results. Lower tiers run only if all higher tiers can not handle the query.
- Pluggable: write custom providers in Kotlin or Swift.
- Suggestions resolve to a `W3WAddress` via `ResolvableSearchProvider`.

## Supported platforms

- **Android**: `minSdk` 24, `compileSdk` 35, JVM target 11.
- **iOS**: `iosArm64`, `iosSimulatorArm64`. Shipped as `W3WKotlinSearchWrapper.xcframework` via [KMMBridge](https://touchlab.co/kmmbridge) (SPM, Swift tools 5.8, iOS 14+).

## Installation

### Gradle (Android / KMP `commonMain`)

```kotlin
dependencies {
    implementation("com.what3words:w3w-kmp-search-wrapper:<version>")
}
```

Use the latest tag from [GitHub Releases](https://github.com/what3words/w3w-kmp-search-wrapper/releases).

### Swift Package Manager

The iOS framework is published via KMMBridge to a Swift Package Manager repository. Add it through **File → Add Package Dependencies** in Xcode using the SPM repo URL listed on the [Releases page](https://github.com/what3words/w3w-kmp-search-wrapper/releases) and import `W3WKotlinSearchWrapper`.

For step-by-step instructions on building and linking the `W3WKotlinSearchWrapper.xcframework` into an Xcode project, see the internal guide: [KMM Sample Library — W3W Address Repository](https://www.notion.so/what3words/KMM-Sample-Library-W3W-Address-Repository-2774eb2734f780828eacf1ac938b4b71?source=copy_link).

## Core API

| Type | Responsibility |
| --- | --- |
| `W3WSearchClient` | Entry point. Holds installed plugins, dispatches `search(query)` across priority tiers, and routes `resolve(suggestion)` to the originating provider. |
| `W3WSearchClient.Config` | DSL receiver used at construction. Exposes `install(plugin, config, priority) { ... }`. |
| `SearchProvider` | Interface a provider implements: `providerId`, `canHandle(query)`, `executeSearch(query)`. |
| `ResolvableSearchProvider` | A `SearchProvider` that can also turn a `SearchSuggestion` into a `ResolvedAddress` via `resolve(data)`. |
| `SearchPlugin<TConfig, TProvider>` | Factory that builds a provider from `(config, textDataSource)`. Use `SimpleSearchPlugin` when a default config makes sense. |
| `SearchResult` | Sealed type with two variants: `SearchSuggestion` (needs `resolve`) and `ResolvedAddress` (carries a `W3WAddress`). Exposes `title`, `subtitle`, and an `extras: Map<String, String>` bag with keys like `EXTRAS_KEY_TITLE`, `EXTRAS_KEY_SUBTITLE`, `EXTRAS_KEY_DISTANCE_TO_FOCUS`. |
| `W3WResult<T>` | `Success(value)` or `Failure(error, message)`. The return shape for every public call. |

## Quickstart (Kotlin)

`search` and `resolve` are `suspend` functions, so call them from a coroutine.

```kotlin
val client = W3WSearchClient(textDataSource) {
    install(ThreeWordAddressSearch, priority = 10)
    install(CoordinatesSearch, priority = 9)
    install(BritishNationalGridSearch, priority = 7)
    install(MayBeAThreeWordAddressSearch, priority = 1)
    if (mapType == "Google") {
        install(GooglePlacesSearch, GooglePlacesConfig(apiKey = BuildConfig.GOOGLE_PLACES_TOKEN), priority = 1)
    } else {
        install(MapboxSearch, MapboxConfig(apiKey = BuildConfig.MAPBOX_TOKEN), priority = 1)
    }
}

when (val result = client.search("filled.count.soap")) {
    is W3WResult.Success -> result.value.forEach { println("${it.title}, ${it.subtitle}") }
    is W3WResult.Failure -> println(result.error)
}

// Resolve a SearchSuggestion into a full W3WAddress (suspending call):
val resolved = client.resolve(suggestion)
```

## Quickstart (Swift)

SKIE generates idiomatic Swift bindings. Install plugins with `addPlugin(...)` using each plugin's `asHandle(...)` factory, then `await searchClient.search(query:)`. `onEnum(of:)` below is a SKIE helper for exhaustively switching over Kotlin sealed classes (here, `W3WResult`) from Swift.

```swift
let searchClient = W3WSearchClient(textDataSource)

searchClient.addPlugin(plugin: ThreeWordAddressSearch.shared.asHandle(), priority: 7)
searchClient.addPlugin(plugin: CoordinatesSearch.shared.asHandle(), priority: 8)
searchClient.addPlugin(plugin: BritishNationalGridSearch.shared.asHandle(), priority: 9)
searchClient.addPlugin(plugin: MayBeAThreeWordAddressSearch.shared.asHandle(), priority: 1)

let result: W3WResult<NSArray> = try await searchClient.search(query: "filled.count.soap")

switch onEnum(of: result) {
case .success(let success):
    let items = (success.value as? [SearchResult]) ?? []
    items.forEach { print($0.title ?? "") }
    // For SearchSuggestion items: try await searchClient.resolve(data: suggestion)
case .failure(let failure):
    print("Error: \(failure.message ?? "?")")
}
```

See [`iosApp/SearchComponent/W3WKmpSearchTree.swift`](iosApp/SearchComponent/W3WKmpSearchTree.swift) for the end-to-end integration with `W3WSwiftComponentsSearch`.

## Built-in search providers

| Plugin | Config | Resolvable | Notes                                                                            |
| --- | --- | --- |----------------------------------------------------------------------------------|
| `ThreeWordAddressSearch` | `ThreeWordAddressSearchConfig` (default available) | Yes | what3words autosuggest.                                                          |
| `MayBeAThreeWordAddressSearch` | `MayBeAThreeWordAddressSearchConfig` (default available) | No | Fuzzy "did you mean" three-word address suggestions.                             |
| `CoordinatesSearch` | `CoordinatesSearchConfig` (default available) | No | Parses DD / DDM / DMS coordinate strings to `W3WAddress`.                        |
| `BritishNationalGridSearch` | `BritishNationalGridSearchConfig` (default available) | No | OS grid references and easting/northing to `W3WAddress`.                         |
| `MapboxSearch` | `MapboxConfig(apiKey, language, minQueryLength, maxResults, includedRegionCodes, focus)` | Yes | Mapbox Search Box v1 (`/suggest` + `/retrieve`). Requires a Mapbox access token. |
| `GooglePlacesSearch` | `GooglePlacesConfig(apiKey, language, useSessionTokens, minQueryLength, maxResults, locationBias, origin, includedRegionCodes, headers)` | Yes | Google Places Autocomplete + Place Details. Requires an API key.                 |

## Updating provider configuration at runtime

A `W3WSearchClient` is usually constructed once per `ViewModel`, but provider settings often need to change as the user interacts with the UI (e.g. toggling country clipping, switching languages, adjusting `maxResults`). Each installed provider exposes its live config through an extension property on `W3WSearchClient`, so you can mutate or swap it without rebuilding the client. Changes take effect on the next `search(query)` call.

| Provider | Extension on `W3WSearchClient` | Mutation style |
| --- | --- | --- |
| `ThreeWordAddressSearch` | `threeWordAddressConfig` | Field-level (`var` properties) or full swap |
| `MayBeAThreeWordAddressSearch` | `mayBeAThreeWordAddressConfig` | Field-level or full swap |
| `CoordinatesSearch` | `coordinatesConfig` | Field-level or full swap |
| `BritishNationalGridSearch` | `britishNationalGridConfig` | Field-level or full swap |
| `MapboxSearch` | `mapboxConfig` | Full swap via `copy(...)` (immutable `data class`) |
| `GooglePlacesSearch` | `googlePlacesConfig` | Full swap via `copy(...)` (immutable) |

Each getter returns `null` if the corresponding plugin was not installed. The setter accepts
only non-null values — assigning `null` throws `IllegalArgumentException`. Assigning a
non-null value when the plugin is not installed is a no-op.

### Field-level mutation

For configs with `var` fields, mutate in place:

```kotlin
class SearchViewModel(private val client: W3WSearchClient) : ViewModel() {

    fun onClipToUkToggled(enabled: Boolean) {
        client.threeWordAddressConfig?.clippedCountries =
            if (enabled) listOf(W3WCountry("GB")) else emptyList()
    }

    fun onFocusChanged(coords: W3WCoordinates?) {
        client.threeWordAddressConfig?.focus = coords
    }
}
```

### Swap an immutable config

For `MapboxConfig` and `GooglePlacesConfig` (immutable), assign a `copy(...)`:

```kotlin
client.mapboxConfig = client.mapboxConfig?.copy(
    maxResults = 10,
    includedRegionCodes = listOf("GB"),
)

client.googlePlacesConfig = client.googlePlacesConfig?.copy(
    includedRegionCodes = listOf("GB"),
)
```

You can use the swap-style update on the mutable configs too if you prefer a single transactional update:

```kotlin
client.threeWordAddressConfig = ThreeWordAddressSearchConfig(
    clippedCountries = listOf(W3WCountry("GB")),
    maxResults = 5,
    preferLand = true,
)
```

### Swift

SKIE exposes the same extensions as Swift properties on `W3WSearchClient`:

```swift
searchClient.threeWordAddressConfig?.clippedCountries = [W3WCountry(twoLetterCode: "GB")]

if let current = searchClient.mapboxConfig {
    searchClient.mapboxConfig = current.doCopy(
        maxResults: 10,
        includedRegionCodes: ["GB"]
    )
}
```

### Concurrency notes

- The underlying `var config` is annotated `@Volatile`, so reference swaps are safely published across threads.
- Each `executeSearch` / `resolve` reads `config` into a local snapshot at the start of the call, so an in-flight search will not observe a torn view if you mutate from another thread.
- Field-level mutations on a shared config instance are not individually atomic across multiple fields. If you need to change several fields together while a search may be in flight, prefer the swap-style update (assign a new config instance).

## How does it work?

### Priority tiers

Each plugin is installed with a `priority` integer. Plugins sharing the same value form a tier. When `search(query)` runs:

1. Tiers are tried from highest priority to lowest.
2. Inside a tier, every provider whose `canHandle(query)` returns `true` runs concurrently, and successful results are merged into one list.
3. If no provider in a tier can handle the query, or all of them fail, the next tier is tried.
4. If no tier yields results, the call returns `W3WResult.Failure(ProviderNotFoundException())`.

A typical setup puts free local parsers (what3words, coordinates, BNG) on a high tier and bills geocoders (Mapbox, Google Places) on a lower fallback tier.

### Suggestion to resolved address

`SearchResult` has two shapes:

- **`ResolvedAddress`**: already carries a full `W3WAddress`. Returned by providers that can answer in one step (e.g. `BritishNationalGridSearch`, `CoordinatesSearch`).
- **`SearchSuggestion`**: a display-ready hint (`title`, `subtitle`, `extras`) that still needs a round-trip to become an address. Geocoders like Mapbox and Google Places return these from their suggest/autocomplete endpoints; `extras` carries the provider-specific ID used to look up coordinates.

`client.resolve(suggestion)` finds the originating provider by `providerId`, checks it implements `ResolvableSearchProvider`, and calls its `resolve(data)`. That call fetches coordinates (e.g. Mapbox `/retrieve`, Google Place Details) and converts them to a `W3WAddress` through the shared `W3WTextDataSource`. If the provider is not resolvable, the call returns `W3WResult.Failure(ProviderNotResolvableException())`.

### Custom providers

The plugin system works the same on both platforms. Implement `SearchProvider` (or `ResolvableSearchProvider`) and expose a `SearchPlugin` or `SimpleSearchPlugin`. Do it in Kotlin in the shared module, or directly in Swift in the iOS app.

**Kotlin**

```kotlin
class MyProvider(private val config: MyConfig) : SearchProvider {
    override val providerId = "my-provider"
    override fun canHandle(query: String) = query.length >= 2
    override suspend fun executeSearch(query: String) = safeW3WCall {
        // call your API, build SearchResult.SearchSuggestion or ResolvedAddress
        W3WResult.Success(emptyList())
    }
}

object MySearch : SearchPlugin<MyConfig, SearchProvider>() {
    override fun build(config: MyConfig, textDataSource: W3WTextDataSource) =
        MyProvider(config)
}
```

**Swift**. SKIE renames `executeSearch(query:)` to `__search(query:)` for Swift implementers. The public async API is generated by SKIE's extension on `SearchProvider`.

```swift
private final class AppleMapSearchProvider: SearchProvider {
    var providerId: String { "apple-map-search" }
    func canHandle(query: String) -> Bool { query.count >= 2 }

    func __search(query: String) async throws -> W3WResult<NSArray> {
        // build [SearchResult.SearchSuggestion] from MKLocalSearch
        return W3WResultSuccess<NSArray>(value: NSArray(array: suggestions))
    }
}

final class AppleMapSearch: SimpleSearchPlugin<AppleMapSearchConfig, any SearchProvider> {
    static let shared = AppleMapSearch()
    override func defaultConfig() -> AppleMapSearchConfig { AppleMapSearchConfig() }
    override func build(config: AppleMapSearchConfig,
                        textDataSource: any W3WTextDataSource) -> any SearchProvider {
        AppleMapSearchProvider(config: config)
    }
}
```

Full reference: [`iosApp/SearchComponent/AppleMapSearchProvider.swift`](iosApp/SearchComponent/AppleMapSearchProvider.swift).

## Sample apps

- **`composeApp/`**: Android / Compose Multiplatform sample wiring the client end-to-end.
- **`iosApp/`**: iOS sample consuming the generated `W3WKotlinSearchWrapper.xcframework`. See [`W3WKmpSearchTree.swift`](iosApp/SearchComponent/W3WKmpSearchTree.swift) for the integration entry point.

## License & contact

This project is available under the [MIT License](LICENSE).

Maintained by [what3words](https://what3words.com). For questions or contributions, contact `development@what3words.com` or open an issue on the [GitHub repository](https://github.com/what3words/w3w-kmp-search-wrapper).
