package com.example.movieapp.feature.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.movieapp.feature.search.R
import com.example.movieapp.domain.model.SearchHistory

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onMovieClick: (Int) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.onStart()
    }

    SearchContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::onSearch,
        onDeleteHistory = viewModel::onDeleteHistory,
        onClearAllHistory = viewModel::onClearAllHistory,
        onMovieClick = { movieId ->
            viewModel.onSearch(state.query)
            onMovieClick(movieId)
        }
    )
}

@Composable
fun SearchContent(
    state: SearchState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onDeleteHistory: (String) -> Unit,
    onClearAllHistory: () -> Unit,
    onMovieClick: (Int) -> Unit,
    initialIsFocused: Boolean = false
) {
    var isFocused by remember { mutableStateOf(initialIsFocused) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Custom Search Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isFocused || state.query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onQueryChange("")
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.search_back_desc)
                    )
                }
            }

            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused = it.isFocused },
                placeholder = { Text(stringResource(R.string.search_placeholder), fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = stringResource(R.string.search_clear_desc))
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch(state.query) })
            )
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error, color = MaterialTheme.colorScheme.error)
            }
        } else if (state.query.isEmpty()) {
            if (isFocused) {
                // Discovery Mode (Recent + Trending) - Only when focused
                DiscoverySections(
                    history = state.searchHistory,
                    trending = state.trendingSearches,
                    onDeleteHistory = onDeleteHistory,
                    onClearAllHistory = onClearAllHistory,
                    onSearch = onSearch
                )
            } else {
                // Default Mode - Top Rated Movies
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = stringResource(R.string.search_top_rated_title),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(state.topRatedMovies) { movie ->
                        SearchMovieItem(
                            movie = movie,
                            genres = state.genres,
                            onMovieClick = onMovieClick
                        )
                    }
                }
            }
        } else {
            // Search Results
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.movies) { movie ->
                    SearchMovieItem(
                        movie = movie,
                        genres = state.genres,
                        onMovieClick = onMovieClick
                    )
                }

                if (state.movies.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.search_not_found))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoverySections(
    history: List<SearchHistory>,
    trending: List<String>,
    onDeleteHistory: (String) -> Unit,
    onClearAllHistory: () -> Unit,
    onSearch: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Son Aramalar Section
        if (history.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.search_recent_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                TextButton(onClick = onClearAllHistory) {
                    Text(
                        text = stringResource(R.string.search_clear_all),
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                    )
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history) { item ->
                    SearchChip(
                        text = item.query,
                        onDelete = { onDeleteHistory(item.query) },
                        onClick = { onSearch(item.query) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Trend Aramalar Section
        if (trending.isNotEmpty()) {
            Text(
                text = stringResource(R.string.search_trending_title),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(trending) { term ->
                    SearchChip(
                        text = term,
                        onClick = { onSearch(term) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchChip(
    text: String,
    onDelete: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(4.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontSize = 14.sp)
            if (onDelete != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.search_delete_desc),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onDelete() },
                    tint = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MaterialTheme {
        Surface {
            SearchContent(
                state = SearchState(
                    query = "",
                    trendingSearches = listOf("Batman", "Marvel", "Inception"),
                    searchHistory = listOf(
                        SearchHistory("Inception", 123L),
                        SearchHistory("Interstellar", 124L)
                    )
                ),
                onQueryChange = {},
                onSearch = {},
                onDeleteHistory = {},
                onClearAllHistory = {},
                onMovieClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecentSearchesPreview() {
    MaterialTheme {
        Surface {
            SearchContent(
                state = SearchState(
                    query = "",
                    trendingSearches = listOf("Marvel", "Inception", "Interstellar"),
                    searchHistory = listOf(
                        SearchHistory("Batman", 1L),
                        SearchHistory("Avengers", 2L),
                        SearchHistory("Joker", 3L)
                    )
                ),
                onQueryChange = {},
                onSearch = {},
                onDeleteHistory = {},
                onClearAllHistory = {},
                onMovieClick = {},
                initialIsFocused = true
            )
        }
    }
}
