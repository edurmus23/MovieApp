package com.example.movieapp.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.example.movieapp.navigation.*
import com.example.movieapp.core.ui.theme.MovieAppTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MovieAppScreen(
    navigator: Navigator,
    entryProviderInstallers: Set<EntryProviderInstaller>
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    MovieAppTheme {
        val adaptiveInfo = currentWindowAdaptiveInfo()
        
        BoxWithConstraints {
            val screenWidth = maxWidth
            val isTablet = screenWidth >= 600.dp
            
            val currentRoute = navigator.backStack.lastOrNull()
            val showNav = currentRoute in listOf(Movies, Search, Profile, MyLists) || currentRoute is Favorites
            
            val isDetailPaneVisible = navigator.backStack.any { 
                it is MovieDetail || it is ListDetail || it is PublicProfile || it is Settings || it is RatedMovies 
            }
            
            val dynamicDirective = remember(adaptiveInfo, isDetailPaneVisible, screenWidth, isTablet) {
                val baseDirective = calculatePaneScaffoldDirective(adaptiveInfo)
                if (isTablet) {
                    baseDirective.copy(
                        maxHorizontalPartitions = if (isDetailPaneVisible) 2 else 1,
                        defaultPanePreferredWidth = if (isDetailPaneVisible) screenWidth / 2 else screenWidth
                    )
                } else {
                    baseDirective
                }
            }
            
            val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = dynamicDirective)

            Row(modifier = Modifier.fillMaxSize()) {
                if (showNav && isTablet) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        header = { Spacer(Modifier.height(8.dp)) },
                        content = {
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items.forEach { screen ->
                                    val isSelected = when (screen) {
                                        BottomNavItem.Home -> currentRoute == Movies
                                        BottomNavItem.Search -> currentRoute == Search
                                        BottomNavItem.Favorites -> currentRoute is Favorites
                                        BottomNavItem.Profile -> currentRoute == Profile
                                        else -> false
                                    }
                                    
                                    val title = when (screen) {
                                        BottomNavItem.Home -> "Ana Sayfa"
                                        BottomNavItem.Search -> "Arama"
                                        BottomNavItem.Favorites -> "Favoriler"
                                        BottomNavItem.Profile -> "Profil"
                                        else -> ""
                                    }

                                    NavigationRailItem(
                                        icon = {
                                            Icon(
                                                imageVector = screen.icon,
                                                contentDescription = title
                                            )
                                        },
                                        label = { Text(title) },
                                        selected = isSelected,
                                        onClick = {
                                            val targetRoute = when (screen) {
                                                BottomNavItem.Home -> Movies
                                                BottomNavItem.Search -> Search
                                                BottomNavItem.Favorites -> Favorites()
                                                BottomNavItem.Profile -> Profile
                                                else -> Movies
                                            }
                                            navigator.navigateAndClear(targetRoute)
                                        },
                                        colors = NavigationRailItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                        )
                                    )
                                }
                            }
                        }
                    )
                }

                Scaffold(
                    bottomBar = {
                        if (showNav && !isTablet) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ) {
                                items.forEach { screen ->
                                    val isSelected = when (screen) {
                                        BottomNavItem.Home -> currentRoute == Movies
                                        BottomNavItem.Search -> currentRoute == Search
                                        BottomNavItem.Favorites -> currentRoute is Favorites
                                        BottomNavItem.Profile -> currentRoute == Profile
                                        else -> false
                                    }
                                    
                                    val title = when (screen) {
                                        BottomNavItem.Home -> "Ana Sayfa"
                                        BottomNavItem.Search -> "Arama"
                                        BottomNavItem.Favorites -> "Favoriler"
                                        BottomNavItem.Profile -> "Profil"
                                        else -> ""
                                    }

                                    NavigationBarItem(
                                        icon = {
                                            Icon(
                                                imageVector = screen.icon,
                                                contentDescription = title
                                            )
                                        },
                                        label = { Text(title) },
                                        selected = isSelected,
                                        onClick = {
                                            val targetRoute = when (screen) {
                                                BottomNavItem.Home -> Movies
                                                BottomNavItem.Search -> Search
                                                BottomNavItem.Favorites -> Favorites()
                                                BottomNavItem.Profile -> Profile
                                                else -> Movies
                                            }
                                            navigator.navigateAndClear(targetRoute)
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavDisplay<NavKey>(
                            modifier = Modifier.fillMaxSize(),
                            backStack = navigator.backStack,
                            onBack = { navigator.goBack() },
                            sceneStrategies = listOf(listDetailStrategy),
                            entryProvider = entryProvider {
                                entryProviderInstallers.forEach { installer ->
                                    installer()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieAppScreenPreview() {
    val mockNavigator = Navigator().apply {
        navigateAndClear(Movies)
    }
    MovieAppScreen(
        navigator = mockNavigator,
        entryProviderInstallers = setOf({
            entry<Movies> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ana Sayfa (Önizleme)")
                }
            }
        })
    )
}
