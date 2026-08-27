# Root-Level Modularization Plan

This plan fixes the previous step by ensuring `network` and `navigation` modules are located at the root level of the project, not inside a `core` sub-folder.

## Proposed Changes

### 1. Project Structure Adjustment
- **[DELETE]** Remove `:core:network` from `settings.gradle.kts` and delete the folder.
- **[NEW]** Add `include(":network")` to `settings.gradle.kts`.
- **[NEW]** Add `include(":navigation")` to `settings.gradle.kts`.

### 2. Network Module Setup (Root Level)
- Create `/network/build.gradle.kts` with Retrofit/OkHttp dependencies.
- **[MOVE]** `MovieApiService.kt` -> `network/src/main/java/com/example/movieapp/network/`.
- **[NEW]** `NetworkModule.kt` inside `:network` to provide network dependencies.

### 3. Navigation Module Setup (Root Level)
- Create `/navigation/build.gradle.kts` with Navigation3 dependencies.
- **[MOVE]** `Navigator.kt` & `NavKey.kt` -> `navigation/src/main/java/com/example/movieapp/navigation/`.
- **[MOVE]** `BottomNavItem.kt` -> `navigation/src/main/java/com/example/movieapp/navigation/`.

### 4. Dependency Refactoring
- Update `:core:data` to depend on `:network`.
- Update all `:feature` modules and `:app` to depend on `:navigation`.
- Update `:core:ui` to depend on `:navigation`.

## Verification Plan
- Clean and rebuild project: `./gradlew clean assembleDebug`.
- Verify no import errors remain.
