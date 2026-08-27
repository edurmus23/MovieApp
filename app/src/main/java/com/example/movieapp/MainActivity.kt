package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movieapp.navigation.EntryProviderInstaller
import com.example.movieapp.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var entryProviderInstallers: Set<@JvmSuppressWildcards EntryProviderInstaller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        
        // Android 13+ Notification Permission
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            androidx.core.app.ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        setContent {
            MovieAppScreen(
                navigator = navigator,
                entryProviderInstallers = entryProviderInstallers
            )
        }
    }
    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // Güncel intent'i kaydet
        handleIntent(intent) // Uygulama açıkken tıklanırsa
    }

    private fun handleIntent(intent: android.content.Intent?) {
        android.util.Log.d("DeepLink", "handleIntent tetiklendi: ${intent?.data}")
        
        // 1. Önce doğrudan gelen verileri (extras) kontrol ediyoruz (Firebase background bildirimleri için)
        val movieIdFromExtras = intent?.getStringExtra("movie_id")
        if (!movieIdFromExtras.isNullOrBlank()) {
            android.util.Log.d("DeepLink", "Extras içinde Movie ID bulundu: $movieIdFromExtras")
            movieIdFromExtras.toIntOrNull()?.let {
                navigator.navigate(com.example.movieapp.navigation.MovieDetail(it))
                return // Navigasyon yapıldıysa bitir
            }
        }

        // 2. URI (Deep Link) kontrolü
        val uri = intent?.data
        if (uri != null) {
            android.util.Log.d("DeepLink", "URI Yakalandı: $uri, Scheme: ${uri.scheme}, Host: ${uri.host}, Path: ${uri.path}")
            
            val targetKey = when {
                // movieapp://movie/550 VEYA https://movieapp.com/movie/550 formatı
                (uri.scheme == "movieapp" && uri.host == "movie") ||
                        ((uri.scheme == "http" || uri.scheme == "https") && uri.host == "movieapp.com" && uri.path?.startsWith("/movie") == true) -> {
                    val movieId = uri.lastPathSegment?.toIntOrNull()
                    android.util.Log.d("DeepLink", "Film ID bulundu: $movieId")
                    movieId?.let { com.example.movieapp.navigation.MovieDetail(it) }
                }

                // movieapp://profile VEYA https://movieapp.com/profile formatı
                (uri.scheme == "movieapp" && uri.host == "profile") ||
                        ((uri.scheme == "http" || uri.scheme == "https") && uri.host == "movieapp.com" && uri.path?.startsWith("/profile") == true) -> {
                    val userId = if (uri.pathSegments.size > 1) uri.lastPathSegment else null
                    if (userId != null) {
                        android.util.Log.d("DeepLink", "Kamu profiline yönlendiriliyor: $userId")
                        com.example.movieapp.navigation.PublicProfile(userId)
                    } else {
                        android.util.Log.d("DeepLink", "Kendi profil sayfasına yönlendiriliyor")
                        com.example.movieapp.navigation.Profile
                    }
                }

                // movieapp://mylists formatı
                (uri.scheme == "movieapp" && uri.host == "mylists") -> {
                    android.util.Log.d("DeepLink", "Listelerim sayfasına yönlendiriliyor")
                    com.example.movieapp.navigation.MyLists
                }

                // movieapp://list/{id} VEYA https://movieapp.com/list/{id} formatı
                (uri.scheme == "movieapp" && uri.host == "list") ||
                        ((uri.scheme == "http" || uri.scheme == "https") && uri.host == "movieapp.com" && uri.path?.startsWith("/list") == true) -> {
                    val listId = uri.lastPathSegment
                    android.util.Log.d("DeepLink", "Liste ID bulundu: $listId")
                    listId?.let { com.example.movieapp.navigation.ListDetail(it, "Paylaşılan Liste", "") }
                }
                else -> {
                    android.util.Log.d("DeepLink", "URI eşleşmedi")
                    null
                }
            }

            // Eğer hedef sayfa bulunduysa oraya yönlendir
            if (targetKey != null) {
                android.util.Log.d("DeepLink", "Navigating to: $targetKey")
                navigator.navigate(targetKey)
                return
            }
        }

        // 2. Eğer link değilse, mevcut bildirim/aksiyon mantığını çalıştır
        val action = intent?.action ?: return
        android.util.Log.d("DeepLink", "Action yakalandı: $action")
        val targetKeyFromAction = when (action) {
            "com.example.movieapp.ACTION_MOVIES" -> com.example.movieapp.navigation.Movies
            "com.example.movieapp.ACTION_SEARCH" -> com.example.movieapp.navigation.Search
            "com.example.movieapp.ACTION_FAVORITES" -> com.example.movieapp.navigation.Favorites()
            "com.example.movieapp.ACTION_ACCOUNT" -> com.example.movieapp.navigation.Profile
            else -> null
        }
        targetKeyFromAction?.let { navigator.navigateAndClear(it) }
    }
}
