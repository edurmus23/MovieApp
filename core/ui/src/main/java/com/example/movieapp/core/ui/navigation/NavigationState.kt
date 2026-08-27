package com.example.movieapp.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

/**
 * State holder for navigation state with multiple backstacks support.
 */
class NavigationState<T : NavKey>(startKey: T) {

    // Maintain a stack for each top level route
    private val topLevelStacks = mutableMapOf<T, SnapshotStateList<T>>()

    init {
        topLevelStacks[startKey] = mutableStateListOf(startKey)
    }

    // Expose the current top level route
    var topLevelKey by mutableStateOf(startKey)
        private set

    // The flattened back stack for NavDisplay
    val backStack = mutableStateListOf<T>(startKey)

    private fun updateBackStack() {
        backStack.clear()
        topLevelStacks[topLevelKey]?.let { backStack.addAll(it) }
    }

    fun addTopLevel(key: T) {
        if (topLevelStacks[key] == null) {
            topLevelStacks[key] = mutableStateListOf(key)
        }
        topLevelKey = key
        updateBackStack()
    }

    fun navigate(key: T) {
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun goBack(): Boolean {
        val currentStack = topLevelStacks[topLevelKey] ?: return false
        if (currentStack.size > 1) {
            currentStack.removeAt(currentStack.size - 1)
            updateBackStack()
            return true
        }
        return false
    }
}

@Composable
fun <T : NavKey> rememberNavigationState(startKey: T): NavigationState<T> {
    return remember { NavigationState(startKey) }
}

/**
 * Helper to convert backstack to NavEntries.
 */
@Composable
fun <T : NavKey> NavigationState<T>.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): List<NavEntry<NavKey>> {
    val decorators = listOf(rememberSaveableStateHolderNavEntryDecorator<NavKey>())
    return rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = decorators,
        entryProvider = entryProvider
    )
}
