package com.example.movieapp.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.example.movieapp.navigation.EntryProviderInstaller
import com.example.movieapp.navigation.Navigator
import com.example.movieapp.shared.di.initKoin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.UIKit.UIViewController

fun initKoinIos() {
    initKoin()
}

fun MainViewController(): UIViewController = ComposeUIViewController {
    val koinApp = rememberKoinApp()
    MovieAppScreen(
        navigator = koinApp.navigator,
        entryProviderInstallers = koinApp.entryProviderInstallers
    )
}

private class KoinAppHolder : KoinComponent {
    val navigator: Navigator by inject()
    val entryProviderInstallers: Set<EntryProviderInstaller> by inject()
}

@Composable
private fun rememberKoinApp(): KoinAppHolder {
    return remember { KoinAppHolder() }
}
