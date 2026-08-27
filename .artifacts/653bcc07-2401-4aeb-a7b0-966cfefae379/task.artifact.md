# Task: Rich Profile Screen Implementation

## Phase 1: UI Shell and Auth Integration
- [ ] Update `AuthRepository` to provide User details (Name, Email)
- [ ] Update `AuthRepositoryImpl` with Firebase integration
- [ ] Create `ProfileViewModel` and `ProfileState`
- [ ] Implement new Profile UI components in `AccountScreen.kt`
    - [ ] `ProfileHeader`
    - [ ] `ProfileStatsRow`
    - [ ] `GenreChips` (Mocked for now)
    - [ ] `RecentlyViewedSection` (Empty list state)
    - [ ] `StatisticsSection` (Mocked for now)
- [ ] Connect `AccountScreen` to `ProfileViewModel`

## Phase 2: Recently Viewed and Data Layer Refactoring
- [x] Create `RecentMovieEntity` and `RecentMovieDao` in `:core:data`
- [ ] Move `FavoriteMovieDao`, `UserListDao` and Entities to `:core:data` to break circular dependency
- [x] Add `RecentMovieDao` to `MovieDatabase` and `AppModule`
- [x] Implement `insertRecentMovie` in `MovieDetailViewModel`
- [ ] Fetch real statistics (Watched count, Watchlist count) in `ProfileViewModel`
- [ ] Fetch recently viewed movies in `ProfileViewModel`
- [ ] Calculate favorite genres in `ProfileViewModel`
