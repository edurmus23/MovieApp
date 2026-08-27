package com.example.movieapp.feature.profile.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.User
import com.example.movieapp.domain.util.Constants
import com.example.movieapp.feature.profile.R
import com.example.movieapp.core.ui.theme.ImdbYellow
import com.example.movieapp.feature.profile.presentation.components.ProfileStatsRow
import com.example.movieapp.feature.profile.presentation.components.SectionHeader
import com.example.movieapp.feature.profile.presentation.components.StatCard
import com.example.movieapp.navigation.Settings
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToRecent: () -> Unit,
    onNavigateToWatched: () -> Unit,
    onNavigateToSocial: () -> Unit,
    onNavigateToFavorites: (Int) -> Unit = {},
    onNavigateToRatedMovies: (String) -> Unit = {},
    onNavigateToPublicProfile: (String) -> Unit = {},
    onMovieClick: (Int) -> Unit = {}
) {
    val profileState = viewModel.state.value
    var showEditNameDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { viewModel.uploadProfilePicture(it.toString()) }
        }
    )

    if (profileState.isLoggedIn) {
        ProfileContent(
            state = profileState,
            onLogout = { viewModel.logout() },
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToRecent = onNavigateToRecent,
            onNavigateToWatched = onNavigateToWatched,
            onNavigateToSocial = onNavigateToSocial,
            onNavigateToFavorites = onNavigateToFavorites,
            onNavigateToRatedMovies = onNavigateToRatedMovies,
            onMovieClick = onMovieClick,
            onProfilePictureClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onEditNameClick = { showEditNameDialog = true },
            onShareClick = { viewModel.shareProfile() },
            onFollowingClick = { viewModel.showFollowingList() }
        )

        if (showEditNameDialog) {
            EditNameDialog(
                currentName = profileState.name,
                onDismiss = { showEditNameDialog = false },
                onConfirm = { newName ->
                    viewModel.updateName(newName)
                    showEditNameDialog = false
                }
            )
        }

        if (profileState.showFollowingSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.hideFollowingList() },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                FollowingListBottomSheet(
                    users = profileState.followingUsers,
                    isLoading = profileState.isFollowingLoading,
                    onUserClick = { userId ->
                        viewModel.hideFollowingList()
                        onNavigateToPublicProfile(userId)
                    }
                )
            }
        }
    } else if (!profileState.isLoading) {
        ProfileNotLoggedInContent(
            onNavigateToLogin = onNavigateToLogin
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ProfileContent(
    state: ProfileState,
    onLogout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToRecent: () -> Unit,
    onNavigateToWatched: () -> Unit,
    onNavigateToSocial: () -> Unit,
    onNavigateToFavorites: (Int) -> Unit,
    onNavigateToRatedMovies: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onProfilePictureClick: () -> Unit,
    onEditNameClick: () -> Unit,
    onShareClick: () -> Unit,
    onFollowingClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
    ) {
        ProfileHeader(
            name = state.name,
            username = state.username,
            joinDate = state.joinDate,
            profilePictureUrl = state.profilePictureUrl,
            onSettingsClick = onNavigateToSettings,
            onProfilePictureClick = onProfilePictureClick,
            onEditNameClick = onEditNameClick,
            onShareClick = onShareClick
        )

        Button(
            onClick = onNavigateToSocial,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.profile_find_friends), fontWeight = FontWeight.Bold)
        }

        ProfileStatsRow(
            watched = state.watchedCount,
            watchlist = state.watchlistCount,
            ratings = state.ratingsCount,
            following = state.followingCount,
            onWatchedClick = onNavigateToWatched,
            onWatchlistClick = { onNavigateToFavorites(1) },
            onRatingsClick = { onNavigateToRatedMovies(state.userId) },
            onFollowingClick = onFollowingClick
        )

        SectionHeader(title = stringResource(R.string.profile_section_favorite_genres))
        GenreChips(genres = state.favoriteGenres)

        RecentlyViewedSection(
            movies = state.recentlyViewed,
            onSeeAllClick = onNavigateToRecent,
            onMovieClick = onMovieClick
        )

        SectionHeader(title = stringResource(R.string.profile_section_statistics))
        StatisticsSection(
            moviesThisMonth = state.moviesThisMonth,
            averageRating = state.averageRating
        )

        Spacer(Modifier.height(32.dp))
        
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(stringResource(R.string.auth_profile_logout_button))
        }
        
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
fun ProfileHeader(
    name: String,
    username: String,
    joinDate: String,
    profilePictureUrl: String?,
    onSettingsClick: () -> Unit,
    onProfilePictureClick: () -> Unit,
    onEditNameClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clickable { onProfilePictureClick() }
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                if (profilePictureUrl != null) {
                    AsyncImage(
                        model = profilePictureUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Sağ alt köşedeki Kamera butonu
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomEnd)
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.padding(6.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        Spacer(Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f, fill = false)
                )
                IconButton(onClick = onEditNameClick, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.profile_edit_name),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = username,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.profile_member_since, joinDate),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row {
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share Profile",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.profile_settings_title),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun EditNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.profile_edit_name_title)) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.profile_name_label)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name) },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(R.string.profile_save_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.profile_cancel_button))
            }
        }
    )
}

@Composable
fun FollowingListBottomSheet(
    users: List<User>,
    isLoading: Boolean,
    onUserClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp, max = 600.dp)
            .padding(bottom = 32.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = stringResource(R.string.profile_following_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(24.dp)
        )
        
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = ImdbYellow)
            }
        } else if (users.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                Spacer(Modifier.height(16.dp))
                Text(stringResource(R.string.profile_following_empty), color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(users) { user ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onUserClick(user.id!!) },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(52.dp)
                                    .border(2.dp, ImdbYellow, CircleShape),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                if (user.profilePictureUrl != null) {
                                    AsyncImage(
                                        model = user.profilePictureUrl,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.padding(12.dp),
                                        tint = ImdbYellow
                                    )
                                }
                            }
                            
                            Spacer(Modifier.width(16.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "@${user.username}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ImdbYellow
                                )
                            }
                            
                            Text(
                                text = stringResource(R.string.profile_view_profile),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = ImdbYellow
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileNotLoggedInContent(
    onNavigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.profile_login_prompt),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.profile_login_button))
        }
    }
}

@Composable
fun GenreChips(genres: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        genres.forEachIndexed { index, genre ->
            val isPrimary = index < 2
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (isPrimary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    text = genre,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RecentlyViewedSection(
    movies: List<MovieDto>,
    onSeeAllClick: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    Column {
        SectionHeader(
            title = stringResource(R.string.profile_section_recently_viewed),
            onSeeAllClick = onSeeAllClick
        )
        
        if (movies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.profile_no_recent_movies),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies) { movie ->
                    RecentMovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                }
            }
        }
    }
}

@Composable
fun RecentMovieCard(movie: MovieDto, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .width(130.dp)
            .clickable { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(2f / 3f)) {
            AsyncImage(
                model = Constants.IMAGE_BASE_URL + movie.posterPath,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            // Karartma Gradyanı
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 200f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700), // Gold/Star color
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticsSection(
    moviesThisMonth: Int,
    averageRating: Double
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            icon = Icons.Default.CalendarMonth,
            label = stringResource(R.string.profile_stat_this_month),
            value = "$moviesThisMonth ${stringResource(R.string.profile_stat_movies)}"
        )
        StatCard(
            icon = Icons.Default.Star,
            label = stringResource(R.string.profile_stat_avg_rating),
            value = String.format(Locale.getDefault(), "%.1f / 5", averageRating)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    MaterialTheme {
        ProfileHeader(
            name = "John Doe",
            username = "@johndoe",
            joinDate = "August 2026",
            profilePictureUrl = null,
            onSettingsClick = {},
            onProfilePictureClick = {},
            onEditNameClick = {},
            onShareClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileStatsRowPreview() {
    MaterialTheme {
        ProfileStatsRow(
            watched = 124,
            watchlist = 45,
            ratings = 89,
            following = 12
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenreChipsPreview() {
    MaterialTheme {
        GenreChips(genres = listOf("Action", "Drama", "Sci-Fi", "Comedy"))
    }
}

@Preview(showBackground = true)
@Composable
fun RecentlyViewedSectionPreview() {
    MaterialTheme {
        RecentlyViewedSection(
            movies = listOf(
                MovieDto(id = 1, title = "Batman", posterPath = "", voteAverage = 8.5),
                MovieDto(id = 2, title = "Inception", posterPath = "", voteAverage = 8.8)
            ),
            onSeeAllClick = {},
            onMovieClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsSectionPreview() {
    MaterialTheme {
        StatisticsSection(
            moviesThisMonth = 12,
            averageRating = 7.8
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileNotLoggedInPreview() {
    MaterialTheme {
        ProfileNotLoggedInContent(onNavigateToLogin = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileContent(
            state = ProfileState(
                name = "John Doe",
                username = "@johndoe",
                watchedCount = 142,
                watchlistCount = 38,
                ratingsCount = 67,
                followingCount = 12,
                favoriteGenres = listOf("Action", "Drama", "Sci-Fi"),
                recentlyViewed = listOf(
                    MovieDto(id = 1, title = "Batman", posterPath = "", voteAverage = 8.5)
                )
            ),
            onLogout = {},
            onNavigateToSettings = {},
            onNavigateToRecent = {},
            onNavigateToWatched = {},
            onMovieClick = {},
            onProfilePictureClick = {},
            onEditNameClick = {},
            onShareClick = {},
            onFollowingClick = {},
            onNavigateToSocial = {},
            onNavigateToFavorites = {},
            onNavigateToRatedMovies = {}
        )
    }
}
