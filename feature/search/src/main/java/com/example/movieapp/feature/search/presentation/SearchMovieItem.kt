package com.example.movieapp.feature.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import com.example.movieapp.core.ui.theme.ImdbYellow
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.util.Constants

@Composable
fun SearchMovieItem(
    movie: MovieDto,
    genres: Map<Int, String>,
    onMovieClick: (Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onMovieClick(movie.id)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(120.dp)

        ) {
            AsyncImage(
                model = Constants.IMAGE_BASE_URL + movie.posterPath,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = ImdbYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = movie.releaseDate?.take(4) ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val genreNames = movie.genreIds.mapNotNull { genres[it] }.joinToString(", ")
                    Text(
                        text = genreNames,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun SearchMovieItemPreview() {
    MaterialTheme {
        SearchMovieItem(
            movie = MovieDto(
                id = 1,
                title = "Batman Begins",
                posterPath = "/poster.jpg",
                overview = "Summary",
                releaseDate = "2005",
                voteAverage = 8.2,
                genreIds = listOf(28, 12)
            ),
            genres = mapOf(28 to "Action", 12 to "Adventure"),
            onMovieClick = {}
        )
    }
}
