# Implementation Plan - Rich Profile Screen with Real Data (Step-by-Step)

The goal is to implement a modern profile screen as requested, using real data from the app's components. We will proceed in phases, starting with the UI and core Auth data, then integrating local database stats and history.

## User Review Required

> [!IMPORTANT]
> - **Statistics:** "İzlenen" (Watched) will be mapped to the user's favorite movies count for now.
> - **Recently Viewed:** We will implement a new local database table to track movies the user has clicked on.
> - **Favorite Genres:** These will be dynamically calculated based on the genres of the user's favorite movies.

## Proposed Changes

### Phase 1: UI Shell and Auth Integration (CURRENT)

#### [MODIFY] [AuthRepository.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/core/domain/src/main/java/com/example/movieapp/domain/repository/AuthRepository.kt)
- Add `currentUserName: String?` and `currentUserEmail: String?` properties.

#### [MODIFY] [AuthRepositoryImpl.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/auth/src/main/java/com/example/movieapp/feature/auth/data/repository/AuthRepositoryImpl.kt)
- Implement the new properties using `firebaseAuth.currentUser`.

#### [NEW] [ProfileViewModel.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/auth/src/main/java/com/example/movieapp/feature/auth/presentation/ProfileViewModel.kt)
- Create a new ViewModel specifically for the profile screen to manage stats, history, and user info.

#### [MODIFY] [AccountScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/auth/src/main/java/com/example/movieapp/feature/auth/presentation/AccountScreen.kt)
- Update `ProfileContent` to match the requested design:
    - **Header:** Real Name/Email from Auth.
    - **Stats:** Placeholders for Phase 1, connected to `ProfileViewModel`.
    - **Genres:** Placeholder chips for Phase 1.
    - **Son Bakılanlar:** Horizontal list with placeholders.
    - **İstatistikler:** Info cards.

### Phase 2: Recently Viewed and Stats Integration

#### [NEW] [RecentMovieEntity.kt] / [RecentMovieDao.kt]
- Create Room entity and DAO to store recently viewed movies.

#### [MODIFY] [MovieDetailViewModel.kt]
- Update to insert the movie into `RecentMovieDao` when details are successfully loaded.

#### [MODIFY] [ProfileViewModel.kt]
- Fetch favorites count from `FavoriteMovieDao`.
- Fetch recently viewed movies from `RecentMovieDao`.
- Calculate top genres from favorites.

## Verification Plan

### Automated Tests
- Build verification for each module.
- Compose Previews for the new UI components.

### Manual Verification
- Log in and verify profile displays your correct email/name.
- Open several movie details, then return to profile to see them in "Son Bakılanlar".
- Favorite a movie and see the "İzlenen" count increment.
