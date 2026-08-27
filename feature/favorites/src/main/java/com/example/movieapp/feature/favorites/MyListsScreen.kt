package com.example.movieapp.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.movieapp.core.ui.theme.ImdbYellow
import com.example.movieapp.domain.model.UserList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListsScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onListClick: (String, String) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showCreateListSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorites_lists_tab_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.favorites_back_desc))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateListSheet = true },
                containerColor = ImdbYellow,
                contentColor = Color.Black,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.favorites_create_list_desc))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
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
                        onDelete = { viewModel.onDeleteList(list.id) }
                    )
                }

                // Suggestion Card
                item {
                    CreateNewListSuggestion(onClick = { showCreateListSheet = true })
                }
            }
        }
    }

    if (showCreateListSheet) {
        CreateListBottomSheet(
            onDismiss = { showCreateListSheet = false },
            onCreate = { name ->
                viewModel.onCreateList(name)
                showCreateListSheet = false
            }
        )
    }
}
