# Fix YouTubePlayerView IllegalStateException

The application is crashing with `java.lang.IllegalStateException: YouTubePlayerView: If you want to initialize this view manually, you need to set 'enableAutomaticInitialization' to false.`
This happens because `YouTubePlayerView` is being initialized manually in the `factory` block of `AndroidView`, while its automatic initialization is still enabled by default.

## Proposed Changes

### feature:movies

#### [MODIFY] [YouTubePlayer.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/movies/src/main/java/com/example/movieapp/feature/movies/presentation/components/YouTubePlayer.kt)
- Set `enableAutomaticInitialization = false` on the `YouTubePlayerView` before calling `initialize` to fix the crash.
- Implement the `update` block in `AndroidView` to handle `youtubeVideoId` changes correctly, ensuring the player loads the new video when the ID updates.
- Use a `remember`ed state to keep track of the `YouTubePlayer` instance for use in the `update` block.

## Verification Plan

### Manual Verification
- Deploy the app and navigate to a movie detail screen that uses `MovieTrailerPlayer`.
- Verify that the crash no longer occurs.
- Verify that the trailer plays correctly.
- Verify that if the `youtubeVideoId` changes, the player updates (if applicable).
