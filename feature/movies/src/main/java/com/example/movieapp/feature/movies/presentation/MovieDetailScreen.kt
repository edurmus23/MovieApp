package com.example.movieapp.feature.movies.presentation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.feature.rating.presentation.components.InteractiveRatingBar
import com.example.movieapp.feature.movies.R
import coil.compose.AsyncImage
import com.example.movieapp.core.ui.components.SectionHeader
import com.example.movieapp.feature.movies.presentation.components.MovieTrailerPlayer
import com.example.movieapp.feature.movies.presentation.components.isValidYouTubeVideoId
import com.example.movieapp.core.ui.components.shimmerEffect
import com.example.movieapp.core.ui.theme.ImdbYellow
import com.example.movieapp.domain.model.GenreDto
import com.example.movieapp.domain.model.MovieDetailDto
import com.example.movieapp.domain.model.UserList
import com.example.movieapp.domain.model.hasProviders
import com.example.movieapp.domain.util.Constants
import androidx.compose.ui.platform.LocalUriHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onMovieClick: (Int) -> Unit = {}
) {
    val state = viewModel.state.value
    val userLists = viewModel.userLists.value
    var showListSheet by remember { mutableStateOf(false) }
    var showCreateListDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
    }

    LaunchedEffect(viewModel.authError) {
        viewModel.authError.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    MovieDetailContent(
        state = state,
        onBackClick = onBackClick,
        onFavoriteClick = viewModel::toggleFavorite,
        onWatchedClick = viewModel::toggleWatched,
        onShareClick = viewModel::shareMovie,
        onAddToListClick = { showListSheet = true },
        onRateMovie = viewModel::rateMovie,
        onMovieClick = onMovieClick,
        onTrailerError = viewModel::onTrailerError,
        showBackIcon = true
    )
    //
    if (showListSheet) {
        ModalBottomSheet(
            onDismissRequest = { showListSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            AddToListSheetContent(
                lists = userLists,
                onListClick = { listId ->
                    viewModel.addMovieToList(listId)
                    showListSheet = false
                },
                onCreateNewClick = {
                    showListSheet = false
                    showCreateListDialog = true // Diyaloğu aç
                }
            )
        }
    }
    if (showCreateListDialog) {
        AlertDialog(
            onDismissRequest = { showCreateListDialog = false },
            title = { Text("Yeni Liste") },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("Liste Adı") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newListName.isNotBlank()) {
                            viewModel.createList(newListName)
                            newListName = ""
                            showCreateListDialog = false
                            showListSheet = true // Listeyi tekrar aç ki yeni listeyi görsün
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ImdbYellow)
                ) {
                    Text("Oluştur", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateListDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
}
//Listeye film ekleme. 
@Composable
fun AddToListSheetContent(
    lists: List<UserList>,
    onListClick: (String) -> Unit,
    onCreateNewClick: () -> Unit // Yeni liste açarken
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.movie_detail_add_to_list_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        ListItem(
            headlineContent = { Text("Yeni liste oluştur...", color = ImdbYellow) },
            leadingContent = { Icon(Icons.Default.Add, contentDescription = null, tint = ImdbYellow) },
            modifier = Modifier.clickable { onCreateNewClick() }
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f)
        )
        
        if (lists.isEmpty()) {
            Text(
                text = stringResource(R.string.movie_detail_no_lists_error),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            lists.forEach { list ->
                ListItem(
                    headlineContent = { Text(list.name) },
                    leadingContent = { Icon(Icons.Default.List, contentDescription = null) },
                    modifier = Modifier.clickable { onListClick(list.id) }
                )
            }
        }
    }
}

@Composable
fun MovieDetailShimmer() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .shimmerEffect()
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(30.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(width = 80.dp, height = 32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            repeat(5) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun MovieDetailContent(
    state: MovieDetailState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onWatchedClick: () -> Unit,
    onShareClick: () -> Unit,
    onAddToListClick: () -> Unit,
    onRateMovie: (Int) -> Unit,
    onMovieClick: (Int) -> Unit,
    onTrailerError: () -> Unit,
    showBackIcon: Boolean = true
) {
    Log.e("MovieDetailScreen", "MovieDetailContent Composing: movie=${state.movie?.title}, trailerKey=${state.trailerKey}, isLoading=${state.isLoading}")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            MovieDetailShimmer()
        }

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }

        state.movie?.let { movie ->
            val uriHandler = LocalUriHandler.current
            val showTrailerPlayer = !state.isLoading &&
                !state.isTrailerError &&
                !state.trailerKey.isNullOrBlank() &&
                isValidYouTubeVideoId(state.trailerKey)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Fixed header: video player with overlay controls (kept outside scroll for WebView)
                key(movie.id) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                    ) {
                        if (showTrailerPlayer) {
                            MovieTrailerPlayer(
                                youtubeVideoId = state.trailerKey,
                                modifier = Modifier.fillMaxSize(),
                                onError = onTrailerError
                            )
                        } else {
                            AsyncImage(
                                model = Constants.IMAGE_BASE_URL + (movie.backdropPath ?: movie.posterPath),
                                contentDescription = movie.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            if (state.isTrailerError && !state.trailerKey.isNullOrBlank()) {
                                IconButton(
                                    onClick = {
                                        uriHandler.openUri("https://www.youtube.com/watch?v=${state.trailerKey}")
                                    },
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = stringResource(R.string.movie_detail_trailer_error),
                                        tint = Color.White,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            } else if (state.trailerKey.isNullOrBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    MaterialTheme.colorScheme.background
                                                ),
                                                startY = 400f
                                            )
                                        )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.TopCenter),
                            horizontalArrangement = if (showBackIcon) Arrangement.SpaceBetween else Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (showBackIcon) {
                                IconButton(
                                    onClick = onBackClick,
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = stringResource(R.string.movie_detail_back_desc),
                                        tint = Color.White
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = onAddToListClick,
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlaylistAdd,
                                        contentDescription = stringResource(R.string.movie_detail_add_to_list_desc),
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = onShareClick,
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = stringResource(R.string.movie_detail_share_desc),
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = onWatchedClick,
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (state.isWatched) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = stringResource(R.string.movie_detail_watched_desc),
                                        tint = if (state.isWatched) ImdbYellow else Color.White
                                    )
                                }

                                IconButton(
                                    onClick = onFavoriteClick,
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = stringResource(R.string.movie_detail_favorite_desc),
                                        tint = if (state.isFavorite) Color.Red else Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // Scrollable content below the player
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Rating Info
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = ImdbYellow,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = String.format("%.1f", movie.voteAverage),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "/10",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Duration Info
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            val runtime = movie.runtime ?: 0
                            val hours = runtime / 60
                            val minutes = runtime % 60
                            val runtimeText = when {
                                hours > 0 && minutes > 0 -> stringResource(R.string.movie_detail_runtime_hours_unit, hours, minutes)
                                hours > 0 -> stringResource(R.string.movie_detail_runtime_hours_only_unit, hours)
                                else -> stringResource(R.string.movie_detail_runtime_minutes_only_unit, minutes)
                            }
                            Text(
                                text = runtimeText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        // Release Date
                        Text(
                            text = movie.releaseDate?.take(4) ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (state.ratingCount > 0L) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = ImdbYellow,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.movie_detail_app_rating),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format("%.1f", state.globalRating),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = ImdbYellow
                            )
                            Text(
                                text = " / 5 (${state.ratingCount} oy)",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Genres
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        movie.genres.forEach { genre ->
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                            ) {
                                Text(
                                    text = genre.name,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (state.watchProviders?.hasProviders() == true) {
                        WatchProvidersSection(
                            providers = state.watchProviders,
                            onProviderClick = { url ->
                                uriHandler.openUri(url)
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    } else if (state.watchProvidersChecked && !state.isLoading) {
                        Text(
                            text = stringResource(R.string.movie_detail_watch_providers_unavailable),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    SectionHeader(
                        title = stringResource(R.string.movie_detail_summary_label), 
                        modifier = Modifier.padding(horizontal = 0.dp)
                    )
                    
                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        lineHeight = 26.sp,
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    SectionHeader(title = stringResource(R.string.movie_detail_your_rating), modifier = Modifier.padding(horizontal = 0.dp))
                    
                    InteractiveRatingBar(
                        currentRating = state.userRating,
                        onRatingSelected = onRateMovie,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )

                    if (state.similarMovies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(32.dp))
                        SectionHeader(
                            title = stringResource(R.string.movie_detail_similar_movies),
                            modifier = Modifier.padding(horizontal = 0.dp)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.similarMovies) { similarMovie ->
                                com.example.movieapp.feature.movies.presentation.components.MovieItem(
                                    movie = similarMovie,
                                    isFavourite = false, // We could potentially check this but let's keep it simple for now
                                    onToggleFavourite = {},
                                    onItemClick = { onMovieClick(similarMovie.id) },
                                    modifier = Modifier.width(150.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
fun WatchProvidersSection(
    providers: com.example.movieapp.domain.model.WatchCountryProviderDto,
    onProviderClick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(
            title = stringResource(R.string.movie_detail_watch_providers_title),
            modifier = Modifier.padding(horizontal = 0.dp)
        )

        providers.flatrate?.takeIf { it.isNotEmpty() }?.let {
            WatchProviderRow(
                title = stringResource(R.string.movie_detail_watch_providers_stream),
                providers = it,
                onClick = { providers.link?.let(onProviderClick) }
            )
        }

        providers.rent?.takeIf { it.isNotEmpty() }?.let {
            WatchProviderRow(
                title = stringResource(R.string.movie_detail_watch_providers_rent),
                providers = it,
                onClick = { providers.link?.let(onProviderClick) }
            )
        }

        providers.buy?.takeIf { it.isNotEmpty() }?.let {
            WatchProviderRow(
                title = stringResource(R.string.movie_detail_watch_providers_buy),
                providers = it,
                onClick = { providers.link?.let(onProviderClick) }
            )
        }
    }
}

@Composable
fun WatchProviderRow(
    title: String,
    providers: List<com.example.movieapp.domain.model.WatchProviderDto>,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .clickable(onClick = onClick),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            providers.forEach { provider ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(60.dp)
                ) {
                    if (!provider.logoPath.isNullOrBlank()) {
                        AsyncImage(
                            model = Constants.PROVIDER_LOGO_BASE_URL + provider.logoPath,
                            contentDescription = provider.providerName,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = provider.providerName.take(1),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = provider.providerName,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun MovieDetailScreenPreview() {
    MaterialTheme {
        MovieDetailContent(
            state = MovieDetailState(
                movie = MovieDetailDto(
                    id = 1,
                    title = "Batman Begins",
                    posterPath = "/poster.jpg",
                    backdropPath = "/backdrop.jpg",
                    overview = "This is a movie overview. Detailed information about Batman Begins.",
                    releaseDate = "2005",
                    voteAverage = 8.2,
                    runtime = 140,
                    genres = listOf(GenreDto(1, "Action"), GenreDto(2, "Adventure"))
                ),
                trailerKey = "neY2xAx9ReA"
            ),
            onBackClick = {}, 
            onFavoriteClick = {},
            onWatchedClick = {},
            onShareClick = {},
            onAddToListClick = {},
            onRateMovie = {},
            onMovieClick = {},
            onTrailerError = {}
        )
    }
}
