package com.example.movieapp.feature.movies.presentation.components

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

private const val TAG = "MovieTrailerPlayer"
private val YOUTUBE_VIDEO_ID_REGEX = Regex("^[a-zA-Z0-9_-]{11}$")

internal fun isValidYouTubeVideoId(videoId: String): Boolean =
    YOUTUBE_VIDEO_ID_REGEX.matches(videoId.trim())

@Composable
fun MovieTrailerPlayer(
    youtubeVideoId: String,
    modifier: Modifier = Modifier,
    onError: () -> Unit = {}
) {
    val videoId = youtubeVideoId.trim()
    val lifecycleOwner = LocalLifecycleOwner.current
    var playerInstance by remember { mutableStateOf<YouTubePlayer?>(null) }

    // Fragman değiştiğinde oynatıcıyı güncelle
    LaunchedEffect(videoId) {
        if (isValidYouTubeVideoId(videoId)) {
            playerInstance?.loadVideo(videoId, 0f)
        }
    }

    if (!isValidYouTubeVideoId(videoId)) {
        Box(modifier = modifier.background(Color.Black).fillMaxSize())
        return
    }

    Box(modifier = modifier.background(Color.Black)) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                YouTubePlayerView(context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    enableAutomaticInitialization = false
                    lifecycleOwner.lifecycle.addObserver(this)

                    val options = IFramePlayerOptions.Builder(context)
                        .controls(1) // Native kontroller (logo dahil) görünür kalsın
                        .autoplay(0)
                        .rel(0)
                        .build()

                    initialize(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                Log.i(TAG, "Player ready for videoId: $videoId")
                                playerInstance = youTubePlayer
                            }

                            override fun onError(
                                youTubePlayer: YouTubePlayer,
                                error: PlayerConstants.PlayerError
                            ) {
                                Log.e(TAG, "YouTube error=$error videoId=[$videoId]")
                                onError()
                            }
                        },
                        handleNetworkEvents = true,
                        playerOptions = options,
                        videoId = videoId
                    )
                }
            },
            onRelease = { view ->
                lifecycleOwner.lifecycle.removeObserver(view)
                view.release()
                playerInstance = null
            }
        )

        // YouTube logosu ve başlığına tıklanıp dışarı yönlendirilmesini engellemek için 
        // şeffaf tıklama alanları oluşturuyoruz. Logo görünür kalır ama tıklanamaz.
        
        // Sağ alt köşe (Logo alanı)
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 40.dp)
                .align(Alignment.BottomEnd)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* Tıklamayı yut */ }
                )
        )

        // Üst alan (Başlık ve paylaşım alanı)
        Box(
            modifier = Modifier
                .size(width = 250.dp, height = 60.dp)
                .align(Alignment.TopStart)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* Tıklamayı yut */ }
                )
        )
    }
}
