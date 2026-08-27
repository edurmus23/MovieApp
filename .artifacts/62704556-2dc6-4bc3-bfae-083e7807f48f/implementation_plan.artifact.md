# Implementation Plan - Firebase Cloud Integration

This plan transitions the app's authentication and data persistence (favorites & lists) from a local Room database to **Firebase Cloud**. This will ensure that user data is synchronized across devices and persisted even if the app is uninstalled.

## User Review Required

> [!CAUTION]
> **Action Required:** You must create a project in the [Firebase Console](https://console.firebase.google.com/), add this Android app (package name: `com.example.movieapp`), and place the generated `google-services.json` file into the `app/` directory.

> [!WARNING]
> This change will replace the local Login/Register system. Existing local users and favorites will no longer be accessible as we switch to Firebase Authentication and Firestore.

## Proposed Changes

### 1. Build Configuration
#### [MODIFY] [libs.versions.toml](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/gradle/libs.versions.toml)
- Add Firebase BOM, Auth, and Firestore dependencies.
- Add Google Services plugin.

#### [MODIFY] [build.gradle.kts (Root)](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/build.gradle.kts)
- Apply the Google Services plugin.

#### [MODIFY] [app/build.gradle.kts](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/app/build.gradle.kts)
- Apply the Google Services plugin and add Firebase dependencies.

### 2. Authentication (:core:data & :core:domain)
#### [MODIFY] [AuthRepository.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/core/domain/src/main/java/com/example/movieapp/domain/repository/AuthRepository.kt)
- Update interface to return Firebase user information or simplified success results.

#### [MODIFY] [AuthRepositoryImpl.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/core/data/src/main/java/com/example/movieapp/data/repository/AuthRepositoryImpl.kt)
- Replace `UserDao` logic with `FirebaseAuth` calls (`signInWithEmailAndPassword`, `createUserWithEmailAndPassword`).

### 3. Cloud Persistence (Favorites & Lists)
#### [NEW] [FirebaseFavoriteRepository.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/core/data/src/main/java/com/example/movieapp/data/repository/FirebaseFavoriteRepository.kt)
- Create a new implementation of `FavouriteRepository` using **Firebase Firestore**.
- **Data Structure:**
    - `users/{userId}/favorites/{movieId}` -> Movie details.
    - `users/{userId}/lists/{listId}` -> List metadata.
    - `users/{userId}/lists/{listId}/movies/{movieId}` -> Movies within lists.

#### [MODIFY] [AppModule.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/core/data/src/main/java/com/example/movieapp/di/AppModule.kt)
- Provide `FirebaseAuth` and `FirebaseFirestore` instances.
- Swap `FavouriteRepositoryImpl` with `FirebaseFavoriteRepository`.

### 4. UI Layer Updates
#### [MODIFY] [AuthViewModel.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/auth/src/main/java/com/example/movieapp/feature/auth/presentation/AuthViewModel.kt)
- Ensure login/register flow works with Firebase callbacks.

## Verification Plan

### Manual Verification
- **Login/Register:** Verify that a new account created in the app appears in the Firebase Console Auth tab.
- **Favorites:** Add a movie to favorites and verify its entry appears in Firestore under the user's ID.
- **Persistence:** Log out, log in on another device (or emulator), and verify favorites are still there.
- **Real-time Sync:** Verify that adding a favorite on one screen updates others instantly via Firestore Snapshots.
