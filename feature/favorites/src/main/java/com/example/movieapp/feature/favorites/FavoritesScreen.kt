package com.example.movieapp.feature.favorites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.movieapp.core.ui.components.shimmerEffect
import com.example.movieapp.core.ui.theme.ImdbYellow
import com.example.movieapp.domain.model.MovieDto
import com.example.movieapp.domain.model.UserList
import com.example.movieapp.domain.util.Constants
import com.example.movieapp.feature.movies.presentation.components.MovieItem

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    initialTab: Int = 0,
    onMovieClick: (Int) -> Unit = {},
    onListClick: (String, String) -> Unit = { _, _ -> },
) {
    val state by viewModel.state.collectAsState()

    FavoritesScreenContent(
        state = state,
        initialTab = initialTab,
        onMovieClick = onMovieClick,
        onListClick = onListClick,
        onRemoveFavourite = viewModel::onRemoveFavourite,
        onDeleteList = viewModel::onDeleteList,
        onCreateList = viewModel::onCreateList,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenContent(
    state: FavoritesState,
    initialTab: Int = 0,
    onMovieClick: (Int) -> Unit = {},
    onListClick: (String, String) -> Unit = { _, _ -> },
    onRemoveFavourite: (MovieDto) -> Unit = {},
    onDeleteList: (String) -> Unit = {},
    onCreateList: (String) -> Unit = {},
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    var showCreateListSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
                    .padding(top = 4.dp)
            ) {
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = ImdbYellow,
                    indicator = {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(selectedTabIndex = selectedTab),
                            width = 40.dp,
                            color = ImdbYellow
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                stringResource(R.string.favorites_tab_title),
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                stringResource(R.string.favorites_lists_tab_title),
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = { showCreateListSheet = true },
                    containerColor = ImdbYellow,
                    contentColor = Color.Black,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.favorites_create_list_desc))
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                0 -> FavoritesTabContent(
                    state = state,
                    onRemoveFavourite = onRemoveFavourite,
                    onMovieClick = onMovieClick
                )
                1 -> ListsTabContent(
                    state = state,
                    onListClick = onListClick,
                    onDeleteList = onDeleteList,
                    onFavoritesClick = { selectedTab = 0 },
                    onCreateNewClick = { showCreateListSheet = true }
                )
            }
        }
    }

    if (showCreateListSheet) {
        CreateListBottomSheet(
            onDismiss = { showCreateListSheet = false },
            onCreate = { name ->
                onCreateList(name)
                showCreateListSheet = false
            }
        )
    }
}

@Composable
fun FavoritesTabContent(
    state: FavoritesState,
    onRemoveFavourite: (MovieDto) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    when {
        state.isLoading -> FavoritesShimmerGrid()
        state.movies.isEmpty() -> EmptyFavoritesView()
        else -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.movies) { movie ->
                    MovieItem(
                        movie = movie,
                        isFavourite = true,
                        onToggleFavourite = { onRemoveFavourite(movie) },
                        onItemClick = { onMovieClick(movie.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ListsTabContent(
    state: FavoritesState,
    onListClick: (String, String) -> Unit,
    onDeleteList: (String) -> Unit,
    onFavoritesClick: () -> Unit,
    onCreateNewClick: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 170.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary Cards
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            SummaryCards(
                totalFolders = state.userLists.size + 1,
                totalMovies = state.totalSavedCount
            )
        }

        // Static Favorites List
        item {
            UserListItem(
                list = UserList(
                    id = "main_favorites",
                    name = stringResource(R.string.favorites_tab_title),
                    movieCount = state.movies.size,
                    userId = "",
                    thumbnailPath = state.movies.firstOrNull()?.posterPath
                ),
                onClick = onFavoritesClick,
                onDelete = {},
                isDeletable = false
            )
        }

        // User Lists
        items(state.userLists) { list ->
            UserListItem(
                list = list,
                onClick = { onListClick(list.id, list.name) },
                onDelete = { onDeleteList(list.id) }
            )
        }

        // Suggestion Card
        item {
            CreateNewListSuggestion(onClick = onCreateNewClick)
        }
    }
}

@Composable
fun SummaryCards(totalFolders: Int, totalMovies: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            title = "TOPLAM KLASÖR",
            value = totalFolders.toString(),
            icon = Icons.Default.Folder,
            valueColor = ImdbYellow,
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "KAYDEDİLEN FİLM",
            value = totalMovies.toString(),
            icon = Icons.Default.LocalMovies,
            valueColor = Color.White,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: ImageVector,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = valueColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (valueColor == Color.White) MaterialTheme.colorScheme.onSurface else valueColor,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun UserListItem(
    list: UserList,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    isDeletable: Boolean = true
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Thumbnail
            if (list.thumbnailPath != null) {
                AsyncImage(
                    model = Constants.IMAGE_BASE_URL + list.thumbnailPath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // Stronger Gradient for Readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.95f)
                            ),
                            startY = 200f
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = list.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${list.movieCount} film",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }

            // Right Arrow
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ImdbYellow.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(18.dp)
            )
            
            // Subtle Delete Action
            if (isDeletable) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CreateNewListSuggestion(onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = null,
                tint = ImdbYellow.copy(alpha = 0.5f),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Yeni liste oluştur",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListBottomSheet(
    onDismiss: () -> Unit,
    onCreate: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var listName by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.favorites_new_list_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            OutlinedTextField(
                value = listName,
                onValueChange = { listName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.favorites_list_name_placeholder), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ImdbYellow,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { if (listName.isNotBlank()) onCreate(listName) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = listName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ImdbYellow,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.favorites_create_button), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FavoritesShimmerGrid() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 170.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = false
    ) {
        items(6) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun EmptyFavoritesView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.favorites_no_favorites_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.favorites_no_favorites_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SummaryCardsPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(Color.Black).padding(16.dp)) {
            SummaryCards(totalFolders = 6, totalMovies = 116)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, name = "User List Item")
@Composable
fun UserListItemPreview() {
    MaterialTheme {
        Box(modifier = Modifier.background(Color.Black).padding(16.dp).width(200.dp)) {
            UserListItem(
                list = UserList(
                    id = "1",
                    name = "Korku Filmleri",
                    movieCount = 12,
                    userId = "user1",
                    thumbnailPath = null
                ),
                onClick = {},
                onDelete = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Favorites Screen - Lists Tab")
@Composable
fun FavoritesScreenListsPreview() {
    MaterialTheme {
        FavoritesScreenContent(
            state = FavoritesState(
                isLoading = false,
                movies = listOf(
                    MovieDto(id = 1, title = "Movie 1", posterPath = ""),
                    MovieDto(id = 2, title = "Movie 2", posterPath = "")
                ),
                userLists = listOf(
                    UserList(id = "1", name = "İzlenecekler", movieCount = 24, userId = "user1"),
                    UserList(id = "2", name = "Marvel Evreni", movieCount = 22, userId = "user1"),
                    UserList(id = "3", name = "2024 Yapımları", movieCount = 9, userId = "user1")
                )
            ),
            initialTab = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    MaterialTheme {
        FavoritesScreenContent(
            state = FavoritesState(
                isLoading = false,
                movies = listOf(
                    MovieDto(id = 1, title = "Movie 1", posterPath = ""),
                    MovieDto(id = 2, title = "Movie 2", posterPath = "")
                ),
                userLists = listOf(
                    UserList(id = "1", name = "My List 1", userId = "user1"),
                    UserList(id = "2", name = "My List 2", userId = "user1")
                )
            )
        )
    }
}
