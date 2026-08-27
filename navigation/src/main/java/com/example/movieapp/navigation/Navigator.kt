package com.example.movieapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

//Uygulama genelinde navigasyonu yöneten sınıftır. Backstack (geri yığını) takibi yapar.
class Navigator(initialKey: MovieNavKey = Movies) {
    val backStack = mutableStateListOf<MovieNavKey>(initialKey)
    //yeni sayfayı ekliyor stacke
    fun navigate(key: MovieNavKey){
        backStack.add(key)
    }
    fun goBack() : Boolean{
        return if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
            true
        } else {
            false
        }
    }
    // Geçmişi temizle ve yeni bir ekrana git (Örn: Login sonrası Ana Sayfa)
    fun navigateAndClear(key: MovieNavKey) {
        backStack.clear()
        backStack.add(key)
    }
}

/**
 * Modüllerin kendi ekranlarını Navigator'a "kaydetmesi" için kullanılan tip tanımı.
 */
typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit