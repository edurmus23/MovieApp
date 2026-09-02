package com.example.movieapp.feature.watched.data

import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.createMovieDto
import dev.gitlive.firebase.firestore.DocumentSnapshot

internal fun DocumentSnapshot.toMovieDtoOrNull(): MovieDto? {
    return createMovieDto(
        id = optionalValue<Long>("id"),
        title = optionalValue<String>("title"),
        posterPath = optionalValue<String>("posterPath") ?: optionalValue<String>("poster_path"),
        backdropPath = optionalValue<String>("backdropPath") ?: optionalValue<String>("backdrop_path"),
        overview = optionalValue<String>("overview"),
        releaseDate = optionalValue<String>("releaseDate") ?: optionalValue<String>("release_date"),
        voteAverage = try { get<Double?>("voteAverage") ?: get<Double?>("vote_average") } catch (_: Exception) { null },
        genreIds = try { get<List<Long>?>("genreIds") ?: get<List<Long>?>("genre_ids") } catch (_: Exception) { null }
    )
}

internal fun MovieDto.toFirestoreData(): Map<String, Any?> = mapOf(
    "id" to id,
    "title" to title,
    "posterPath" to posterPath,
    "poster_path" to posterPath,
    "backdropPath" to backdropPath,
    "overview" to overview,
    "releaseDate" to releaseDate,
    "release_date" to releaseDate,
    "voteAverage" to voteAverage,
    "vote_average" to voteAverage,
    "genreIds" to genreIds,
    "genre_ids" to genreIds
)

private inline fun <reified T> DocumentSnapshot.optionalValue(field: String): T? {
    return try {
        get<T?>(field)
    } catch (_: Exception) {
        null
    }
}
