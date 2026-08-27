package com.example.movieapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.example.movieapp.navigation.*
import com.example.movieapp.core.ui.theme.MovieAppTheme
import com.example.movieapp.navigation.BottomNavItem
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.window.core.layout.WindowWidthSizeClass

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
        val currentRoute = navigator.backStack.lastOrNull()
        val showNav = currentRoute in listOf(Movies, Search, Profile, MyLists) || currentRoute is Favorites
        val adaptiveInfo = currentWindowAdaptiveInfo()
        val isTablet = adaptiveInfo.windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT

        // List-Detail Strategy (Strict 50/50 split based on screen width)
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val isDetailPaneVisible = navigator.backStack.any { 
            it is MovieDetail || it is ListDetail || it is PublicProfile || it is Settings || it is RatedMovies 
        }
        
        val dynamicDirective = remember(adaptiveInfo, isDetailPaneVisible, screenWidth) {
            val baseDirective = calculatePaneScaffoldDirective(adaptiveInfo)
            if (isTablet) {
                baseDirective.copy(
                    // Detay açıksa 2 panele izin ver, yoksa 1 (Full screen list)
                    maxHorizontalPartitions = if (isDetailPaneVisible) 2 else 1,
                    // Her iki paneli de ekran genişliğinin yarısına (50/50) zorla
                    defaultPanePreferredWidth = if (isDetailPaneVisible) screenWidth / 2 else screenWidth
                )
            } else {
                baseDirective
            }
        }
        
        val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = dynamicDirective)

        Row(modifier = Modifier.fillMaxSize()) {
            // TABLET MODU: Yan menü (Navigation Rail)
            if (showNav && isTablet) {
                NavigationRail(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    header = {
                        // İsteğe bağlı: Üst kısma logo veya boşluk eklenebilir
                        Spacer(Modifier.height(8.dp))
                    },
                    content = {
                        // Menü öğelerini dikeyde ortalamak için Column kullanıyoruz
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

                                NavigationRailItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = stringResource(screen.titleResId)
                                        )
                                    },
                                    label = { Text(stringResource(screen.titleResId)) },
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

            // Ana içerik alanı
            Scaffold(
                bottomBar = {
                    // TELEFON MODU: Alt menü (Navigation Bar)
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
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = stringResource(screen.titleResId)
                                        )
                                    },
                                    label = { Text(stringResource(screen.titleResId)) },
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
