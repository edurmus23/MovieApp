# Implementation Plan - Final YouTubePlayer Stabilization

The previous attempts to fix the `YouTubePlayer` failed to make it visible and caused "Invalid video id" errors. This is likely due to race conditions between initialization and video loading, and potential layout issues.

## User Review Required

> [!IMPORTANT]
> I will refactor the `YouTubePlayer` to use a more controlled state management. I will track the `YouTubePlayer` instance and use a `LaunchedEffect` that only loads the video once both the player is ready and the video ID has changed. I will also add explicit layout parameters and a background color to ensure visibility.

## Proposed Changes

### [feature:movies]

#### [MODIFY] [YouTubePlayer.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/movies/src/main/java/com/example/movieapp/feature/movies/presentation/components/YouTubePlayer.kt)
- Create `YouTubePlayerView` in `factory`.
- Use a `mutableStateOf<YouTubePlayer?>` to store the player instance when `onReady` is called.
- Use `LaunchedEffect(youtubeVideoId, playerInstance)` to load the video only when the ID is valid and the player is ready.
- Add `Log.d` to verify ID and state transitions.
- Explicitly set `LayoutParams` to `MATCH_PARENT` to ensure visibility.

#### [MODIFY] [MovieDetailScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/movies/src/main/java/com/example/movieapp/feature/movies/presentation/MovieDetailScreen.kt)
- Ensure the `YouTubePlayer` is correctly conditionally rendered.
- Remove redundant `key` wrapper to prevent renderer crashes from rapid recreation.

## Verification Plan

### Manual Verification
- Check logcat for "YouTubePlayer" tags to see if `onReady` and `loadVideo` are called with correct IDs.
- Verify visibility in the UI.
- Test movie transitions.
