package com.example.movieapp.domain.model

import com.google.gson.annotations.SerializedName

data class VideoResponseDto(
    val id: Int,
    val results: List<VideoDto>
)

data class VideoDto(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String,
    @SerializedName("official")
    val isOfficial: Boolean
)
