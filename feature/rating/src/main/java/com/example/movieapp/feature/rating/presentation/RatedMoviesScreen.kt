package com.example.movieapp.feature.rating.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.movieapp.core.ui.theme.ImdbYellow
import com.example.movieapp.domain.util.Constants
import com.example.movieapp.feature.rating.domain.model.UserRating
import com.example.movieapp.feature.rating.domain.repository.RatingRepository
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class RatedMoviesViewModel @Inject constructor(
    private val ratingRepository: RatingRepository
) : ViewModel() {
    fun getRatedMovies(userId: String) = ratingRepository.getRatedMovies(userId)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatedMoviesScreen(
    userId: String,
    viewModel: RatedMoviesViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val ratedMovies by viewModel.getRatedMovies(userId).collectAsState(initial = emptyList())

    RatedMoviesContent(
        ratedMovies = ratedMovies,
        onMovieClick = onMovieClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatedMoviesContent(
    ratedMovies: List<UserRating>,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Puanladığım Filmler", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (ratedMovies.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Henüz hiç film puanlamadınız.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ratedMovies) { rating ->
                        RatedMovieCard(
                            rating = rating,
                            onClick = { onMovieClick(rating.movieId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RatedMovieCard(
    rating: UserRating,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = Constants.IMAGE_BASE_URL + rating.moviePosterPath,
                contentDescription = null,
                modifier = Modifier
                    .size(width = 60.dp, height = 90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = rating.movieTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = ImdbYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${rating.rating} / 5",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = ImdbYellow
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatedMoviesPreview() {
    MaterialTheme {
        RatedMoviesContent(
            ratedMovies = listOf(
                UserRating(movieId = 1, rating = 4, movieTitle = "Inception", moviePosterPath = ""),
                UserRating(movieId = 2, rating = 5, movieTitle = "Interstellar", moviePosterPath = "")
            ),
            onMovieClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RatedMovieCardPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            RatedMovieCard(
                rating = UserRating(
                    movieId = 1,
                    rating = 4,
                    movieTitle = "Batman Begins",
                    moviePosterPath = ""
                ),
                onClick = {}
            )
        }
    }
}
