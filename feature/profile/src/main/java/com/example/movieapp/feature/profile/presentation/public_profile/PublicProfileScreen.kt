package com.example.movieapp.feature.profile.presentation.public_profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.util.Constants
import com.example.movieapp.feature.profile.R
import com.example.movieapp.core.ui.theme.ImdbYellow
import com.example.movieapp.feature.profile.presentation.components.ProfileStatsRow
import com.example.movieapp.feature.profile.presentation.components.SectionHeader
import com.example.movieapp.feature.profile.presentation.components.StatCard
import java.util.Locale

@Composable
fun PublicProfileScreen(
    userId: String,
    viewModel: PublicProfileViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onListClick: (String, String, String) -> Unit,
    onNavigateToRatedMovies: (String) -> Unit = {}
) {
    val state = viewModel.state.value
    val isFollowing = viewModel.isFollowing.value

    LaunchedEffect(userId) {
        viewModel.loadProfile(userId)
    }

    PublicProfileContent(
        state = state,
        isFollowing = isFollowing,
        onBackClick = onBackClick,
        onMovieClick = onMovieClick,
        onFollowClick = { viewModel.toggleFollow(userId) },
        onListClick = { listId, listName -> onListClick(listId, listName, userId) },
        onRatingsClick = { onNavigateToRatedMovies(userId) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicProfileContent(
    state: PublicProfileState,
    isFollowing: Boolean,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onFollowClick: () -> Unit,
    onListClick: (String, String) -> Unit,
    onRatingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.public_profile_title), fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.public_profile_back_desc))
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = stringResource(R.string.public_profile_share_desc))
                    }
                    IconButton(onClick = { /* More */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.public_profile_more_desc))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
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
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = ImdbYellow)
            } else if (state.error != null) {
                Text(state.error, modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
            } else {
                state.profileData?.let { data ->
                val scrollState = rememberScrollState()
                val coroutineScope = rememberCoroutineScope()
                var listsSectionY by remember { mutableFloatStateOf(0f) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                        // Profile Header with Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            // Banner Gradient
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(ImdbYellow, Color.Transparent)
                                        )
                                    )
                            )
                            
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 0.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .size(110.dp)
                                        .border(4.dp, MaterialTheme.colorScheme.background, CircleShape),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    if (data.profilePictureUrl != null) {
                                        AsyncImage(
                                            model = data.profilePictureUrl,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(60.dp)
                                                .padding(24.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = data.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = data.username,
                                style = MaterialTheme.typography.titleMedium,
                                color = ImdbYellow,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(
                                    Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.Gray
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "${data.joinDate}'den beri üye",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.Gray
                                )
                            }

                            Spacer(Modifier.height(24.dp))

                            Button(
                                onClick = onFollowClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isFollowing) MaterialTheme.colorScheme.surfaceVariant else ImdbYellow,
                                    contentColor = if (isFollowing) MaterialTheme.colorScheme.onSurfaceVariant else Color.Black
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isFollowing) Icons.Default.PersonRemove else Icons.Default.PersonAdd,
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = if (isFollowing) stringResource(R.string.public_profile_unfollow) else stringResource(R.string.public_profile_follow),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }

                        ProfileStatsRow(
                            watched = data.watchedCount,
                            watchlist = data.watchlistCount,
                            ratings = data.ratingsCount,
                            following = data.followingCount,
                            onWatchlistClick = {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(listsSectionY.toInt())
                                }
                            },
                            onRatingsClick = onRatingsClick
                        )

                        // User Lists
                        if (data.userLists.isNotEmpty() || data.ratingsCount > 0) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .onGloballyPositioned { coordinates ->
                                        listsSectionY = coordinates.positionInParent().y
                                    }
                            ) {
                                SectionHeader(
                                    title = stringResource(R.string.profile_lists_title),
                                    onSeeAllClick = { /* See All Lists */ }
                                )
                                
                                Column(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Static Favorites List
                                    if (data.ratingsCount > 0) {
                                        val favoritesTitle = stringResource(R.string.profile_favorites_list_name)
                                        UserListCard(
                                            listId = "favorites",
                                            listName = favoritesTitle,
                                            movieCount = data.ratingsCount,
                                            onClick = { onListClick("favorites", favoritesTitle) }
                                        )
                                    }

                                    data.userLists.forEach { list ->
                                        UserListCard(
                                            listId = list.id,
                                            listName = list.name,
                                            movieCount = list.movieCount,
                                            onClick = { onListClick(list.id, list.name) }
                                        )
                                    }
                                }
                            }
                        }

                        // Favori Türler
                        Column(modifier = Modifier.padding(top = 24.dp)) {
                            SectionHeader(title = stringResource(R.string.profile_section_favorite_genres))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                data.favoriteGenres.forEachIndexed { index, genre ->
                                    val isHighlight = index < 2
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isHighlight) ImdbYellow else MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = if (isHighlight) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                                        border = if (isHighlight) null else BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2f))
                                    ) {
                                        Text(
                                            text = genre,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }

                        // Recently Viewed
                        Column(modifier = Modifier.padding(top = 24.dp)) {
                            SectionHeader(
                                title = stringResource(R.string.public_profile_recently_viewed),
                                onSeeAllClick = { /* See All */ }
                            )

                            if (data.recentlyViewed.isEmpty()) {
                                Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                                    Text(stringResource(R.string.public_profile_no_movies), color = Color.Gray)
                                }
                            } else {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(data.recentlyViewed) { movie ->
                                        PublicMovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                                    }
                                }
                            }
                        }

                        // Statistics
                        Column(modifier = Modifier.padding(top = 24.dp, bottom = 48.dp)) {
                            SectionHeader(title = stringResource(R.string.public_profile_stats_summary_header))
                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                StatCard(
                                    icon = Icons.Default.CalendarMonth,
                                    label = "Bu Ay",
                                    value = "${data.moviesThisMonth} Film"
                                )
                                StatCard(
                                    icon = Icons.Default.Star,
                                    label = "Ortalama Puan",
                                    value = String.format(Locale.getDefault(), "%.1f / 5", data.averageRating)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PublicProfileScreenPreview() {
    MaterialTheme {
        PublicProfileContent(
            state = PublicProfileState(
                profileData = com.example.movieapp.feature.profile.domain.model.ProfileData(
                    name = "Elif İpek",
                    username = "@elifipek",
                    joinDate = "Ağustos 2026",
                    watchedCount = 124,
                    watchlistCount = 45,
                    ratingsCount = 89,
                    favoriteGenres = listOf("Action", "Drama"),
                    recentlyViewed = emptyList(),
                    moviesThisMonth = 12,
                    averageRating = 8.5,
                    userLists = listOf(
                        com.example.movieapp.domain.model.UserList("1", "Favori Filmlerim", 12, "user1"),
                        com.example.movieapp.domain.model.UserList("2", "İzleyeceklerim", 5, "user1")
                    )
                )
            ),
            isFollowing = false,
            onBackClick = {},
            onMovieClick = {},
            onFollowClick = {},
            onListClick = { _, _ -> },
            onRatingsClick = {}
        )
    }
}


@Composable
fun UserListCard(
    listId: String,
    listName: String,
    movieCount: Int,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = ImdbYellow
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = listName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.public_profile_movie_count, movieCount),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun PublicMovieCard(movie: MovieDto, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.height(190.dp)) {
            AsyncImage(
                model = Constants.IMAGE_BASE_URL + movie.posterPath,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = ImdbYellow, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = movie.title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
