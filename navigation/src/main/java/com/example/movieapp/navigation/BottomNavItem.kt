package com.example.movieapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.annotation.StringRes

sealed class BottomNavItem (
    @StringRes val titleResId: Int,
    val icon: ImageVector
){
    object Home : BottomNavItem(R.string.nav_home, Icons.Default.Home)
    object Search : BottomNavItem(R.string.nav_search, Icons.Default.Search)
    object Favorites : BottomNavItem(R.string.nav_favorites, Icons.Default.Favorite)
    object Profile : BottomNavItem(R.string.nav_profile, Icons.Default.AccountCircle)
}