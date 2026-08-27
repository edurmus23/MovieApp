package com.example.movieapp.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.feature.favorites.R
import androidx.compose.ui.tooling.preview.Preview
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.feature.movies.presentation.components.MovieItem
import com.example.movieapp.core.ui.components.SectionHeader

@Composable
fun ListDetailScreen(
    listId: String,
    listName: String,
    userId: String,
    viewModel: FavoritesViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    val movies by viewModel.getMoviesInList(userId, listId).collectAsState(initial = emptyList())

    ListDetailScreenContent(
        listName = listName,
        movies = movies,
        onMovieClick = onMovieClick,
        onBackClick = onBackClick,
        onRemoveFromList = { movieId -> viewModel.onRemoveMovieFromList(listId, movieId) },
        isOwnList = userId.isBlank() || userId == "current_user_logic" // Handle this better if needed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDetailScreenContent(
    listName: String,
    movies: List<MovieDto>,
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onRemoveFromList: (Int) -> Unit,
    isOwnList: Boolean = true
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(listName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.favorites_back_desc)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
            if (movies.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Movie,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.favorites_list_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 170.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(movies) { movie ->
                        MovieItem(
                            movie = movie,
                            isFavourite = true,
                            onToggleFavourite = { 
                                if (isOwnList) onRemoveFromList(movie.id) 
                            },
                            onItemClick = { onMovieClick(movie.id) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListDetailScreenPreview() {
    MaterialTheme {
        ListDetailScreenContent(
            listName = "İzlenecekler",
            movies = listOf(
                MovieDto(id = 1, title = "Movie 1", posterPath = ""),
                MovieDto(id = 2, title = "Movie 2", posterPath = "")
            ),
            onMovieClick = {},
            onBackClick = {},
            onRemoveFromList = {}
        )
    }
}

