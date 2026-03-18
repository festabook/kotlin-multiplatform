package com.daedan.festabook.data.model.response.splash

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IosAppVersionResponse(
    @SerialName("resultCount") val resultCount: Int,
    @SerialName("results") val results: List<Result>,
) {
    @Serializable
    data class Result(
        @SerialName("advisories") val advisories: List<String>,
        @SerialName("appletvScreenshotUrls") val appletvScreenshotUrls: List<String>,
        @SerialName("artistId") val artistId: Int,
        @SerialName("artistName") val artistName: String,
        @SerialName("artistViewUrl") val artistViewUrl: String,
        @SerialName("artworkUrl100") val artworkUrl100: String,
        @SerialName("artworkUrl512") val artworkUrl512: String,
        @SerialName("artworkUrl60") val artworkUrl60: String,
        @SerialName("averageUserRating") val averageUserRating: Double,
        @SerialName("averageUserRatingForCurrentVersion") val averageUserRatingForCurrentVersion: Double,
        @SerialName("bundleId") val bundleId: String,
        @SerialName("contentAdvisoryRating") val contentAdvisoryRating: String,
        @SerialName("currency") val currency: String,
        @SerialName("currentVersionReleaseDate") val currentVersionReleaseDate: String,
        @SerialName("description") val description: String,
        @SerialName("features") val features: List<String>,
        @SerialName("fileSizeBytes") val fileSizeBytes: String,
        @SerialName("formattedPrice") val formattedPrice: String,
        @SerialName("genreIds") val genreIds: List<String>,
        @SerialName("genres") val genres: List<String>,
        @SerialName("ipadScreenshotUrls") val ipadScreenshotUrls: List<String>,
        @SerialName("isGameCenterEnabled") val isGameCenterEnabled: Boolean,
        @SerialName("isVppDeviceBasedLicensingEnabled") val isVppDeviceBasedLicensingEnabled: Boolean,
        @SerialName("kind") val kind: String,
        @SerialName("languageCodesISO2A") val languageCodesISO2A: List<String>,
        @SerialName("minimumOsVersion") val minimumOsVersion: String,
        @SerialName("price") val price: Double,
        @SerialName("primaryGenreId") val primaryGenreId: Int,
        @SerialName("primaryGenreName") val primaryGenreName: String,
        @SerialName("releaseDate") val releaseDate: String,
        @SerialName("releaseNotes") val releaseNotes: String,
        @SerialName("screenshotUrls") val screenshotUrls: List<String>,
        @SerialName("sellerName") val sellerName: String,
        @SerialName("supportedDevices") val supportedDevices: List<String>,
        @SerialName("trackCensoredName") val trackCensoredName: String,
        @SerialName("trackContentRating") val trackContentRating: String,
        @SerialName("trackId") val trackId: Long,
        @SerialName("trackName") val trackName: String,
        @SerialName("trackViewUrl") val trackViewUrl: String,
        @SerialName("userRatingCount") val userRatingCount: Int,
        @SerialName("userRatingCountForCurrentVersion") val userRatingCountForCurrentVersion: Int,
        @SerialName("version") val version: String,
        @SerialName("wrapperType") val wrapperType: String,
    )
}
