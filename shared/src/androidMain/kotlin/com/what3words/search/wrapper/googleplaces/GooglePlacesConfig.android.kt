package com.what3words.search.wrapper.googleplaces

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchConfig
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private const val HEADER_ANDROID_PACKAGE = "X-Android-Package"
private const val HEADER_ANDROID_CERT = "X-Android-Cert"

actual class GooglePlacesConfig actual constructor(
    actual val apiKey: String,
    actual val language: W3WLanguage,
    actual val useSessionTokens: Boolean,
    actual val minQueryLength: Int,
    actual val maxResults: Int,
    actual val locationBias: LocationBias?,
    actual val origin: W3WCoordinates?,
    actual val includedRegionCodes: List<String>,
    actual val headers: Map<String, String?>,
) : SearchConfig() {
    constructor(
        context: Context,
        apiKey: String,
        language: W3WLanguage = W3WRFC5646Language.EN_GB,
        useSessionTokens: Boolean = true,
        minQueryLength: Int = 3,
        maxResults: Int = 5,
        headers: Map<String, String?> = emptyMap(),
        locationBias: LocationBias? = null,
        origin: W3WCoordinates? = null,
        includedRegionCodes: List<String> = emptyList(),
    ) : this(
        apiKey = apiKey,
        language = language,
        useSessionTokens = useSessionTokens,
        minQueryLength = minQueryLength,
        maxResults = maxResults,
        headers = buildMap {
            putAll(headers)
            put(HEADER_ANDROID_PACKAGE, context.packageName)
            put(HEADER_ANDROID_CERT, context.getSigningCertFingerprint())
        },
        locationBias = locationBias,
        origin = origin,
        includedRegionCodes = includedRegionCodes,
    )

    actual fun copy(
        apiKey: String,
        language: W3WLanguage,
        useSessionTokens: Boolean,
        minQueryLength: Int,
        maxResults: Int,
        headers: Map<String, String?>,
        locationBias: LocationBias?,
        origin: W3WCoordinates?,
        includedRegionCodes: List<String>,
    ): GooglePlacesConfig = GooglePlacesConfig(
        apiKey = apiKey,
        language = language,
        useSessionTokens = useSessionTokens,
        minQueryLength = minQueryLength,
        maxResults = maxResults,
        headers = headers,
        locationBias = locationBias,
        origin = origin,
        includedRegionCodes = includedRegionCodes,
    )
}

/**
 * Returns the SHA-1 fingerprint of this app's signing certificate, formatted as
 * colon-separated uppercase hex (e.g. `AB:CD:EF:01:23:…`), or `null` on failure.
 */
private fun Context.getSigningCertFingerprint(): String? {
    val signature = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val packageInfo = try {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
        val signingInfo = packageInfo?.signingInfo ?: return null
        if (signingInfo.hasMultipleSigners()) {
            signingInfo.apkContentsSigners.firstOrNull()
        } else {
            signingInfo.signingCertificateHistory.lastOrNull()
        }
    } else {
        @Suppress("DEPRECATION")
        val packageInfo = try {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
        @Suppress("DEPRECATION")
        packageInfo?.signatures?.firstOrNull()
    } ?: return null

    return try {
        val messageDigest = MessageDigest.getInstance("SHA-1")
        val digest = messageDigest.digest(signature.toByteArray())
        digest.toHexString(HexFormat.UpperCase)
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        null
    }
}
