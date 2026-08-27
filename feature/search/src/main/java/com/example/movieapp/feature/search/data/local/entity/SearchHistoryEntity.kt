package com.example.movieapp.feature.search.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.domain.model.SearchHistory

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    val query : String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toDomain() = SearchHistory(
        query = query,
        timestamp = timestamp
    )
}
