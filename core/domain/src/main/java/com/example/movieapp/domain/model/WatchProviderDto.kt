package com.example.movieapp.domain.model

import com.google.gson.annotations.SerializedName

data class WatchProviderResponseDto(
    val id: Int,
    val results: Map<String, WatchCountryProviderDto>
)

data class WatchCountryProviderDto(
    val link: String?,
    @SerializedName("flatrate")
    val flatrate: List<WatchProviderDto>?,
    @SerializedName("rent")
    val rent: List<WatchProviderDto>?,
    @SerializedName("buy")
    val buy: List<WatchProviderDto>?
)

data class WatchProviderDto(
    @SerializedName("display_priority")
    val displayPriority: Int,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("provider_id")
    val providerId: Int,
    @SerializedName("provider_name")
    val providerName: String
)

fun WatchCountryProviderDto.hasProviders(): Boolean =
    !flatrate.isNullOrEmpty() || !rent.isNullOrEmpty() || !buy.isNullOrEmpty()
