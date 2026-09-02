# Fix Unresolved Reference 'dagger' in :app:message

The build error `Unresolved reference 'dagger'` was caused by the use of Hilt/Dagger annotations (`@Singleton`, `@Inject`, `@ApplicationContext`) in `NotificationHelper.kt`, while the `:app:message` module is configured to use **Koin** for dependency injection.

## Changes Made

### [NotificationHelper.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/app/message/src/androidMain/kotlin/com/example/movieapp/message/util/NotificationHelper.kt)

- Removed Hilt-specific imports and annotations.
- Refactored the constructor to be a plain Kotlin constructor.
- This class is now correctly managed by Koin as defined in `MessageModule.kt`.

```diff
-import dagger.hilt.android.qualifiers.ApplicationContext
-import javax.inject.Inject
-import javax.inject.Singleton
-
-@Singleton
-class NotificationHelper @Inject constructor(
-    @ApplicationContext private val context: Context
-) {
+class NotificationHelper(
+    private val context: Context
+) {
```

## Verification Results

### Automated Tests
- Ran `./gradlew :app:message:compileDebugKotlinAndroid` and the build finished successfully.

### Manual Verification
- Verified that `MessageModule.kt` correctly provides `NotificationHelper` using `androidContext()`:
  ```kotlin
  val messageModule = module {
      single { NotificationHelper(androidContext()) }
  }
  ```
- Verified that `MovieAppMessagingService.kt` continues to use `NotificationHelper` via Koin's `inject()`:
  ```kotlin
  private val notificationHelper: NotificationHelper by inject()
  ```
