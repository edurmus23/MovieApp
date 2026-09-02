# Fix Unresolved Reference 'tooling' for Preview in KMP

The build error `Unresolved reference 'tooling'` occurs because the project is using the Android-specific `androidx.compose.ui.tooling.preview.Preview` import in `commonMain` source sets of a Kotlin Multiplatform (KMP) project. In KMP with Jetbrains Compose, the correct import for `@Preview` in common code is `org.jetbrains.compose.ui.tooling.preview.Preview`.

## User Review Required

> [!NOTE]
> This change updates imports in multiple files across different modules (`core:ui`, `app:ai`, `feature:favorites`, `feature:social`) to ensure consistency and fix build errors in KMP.

## Proposed Changes

### Core UI Module

#### [MODIFY] [SectionHeader.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/core/ui/src/commonMain/kotlin/com/example/movieapp/core/ui/components/SectionHeader.kt)
- Update `androidx.compose.ui.tooling.preview.Preview` import to `org.jetbrains.compose.ui.tooling.preview.Preview`.

### App AI Module

#### [MODIFY] [AiChatScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/app/ai/src/commonMain/kotlin/com/example/movieapp/ai/presentation/AiChatScreen.kt)
- Update `androidx.compose.ui.tooling.preview.Preview` import to `org.jetbrains.compose.ui.tooling.preview.Preview`.

### Feature Favorites Module

#### [MODIFY] [ListDetailScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/favorites/src/commonMain/kotlin/com/example/movieapp/feature/favorites/ListDetailScreen.kt)
- Update `androidx.compose.ui.tooling.preview.Preview` import to `org.jetbrains.compose.ui.tooling.preview.Preview`.

### Feature Social Module

#### [MODIFY] [SocialScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/social/src/commonMain/kotlin/com/example/movieapp/feature/social/presentation/social/SocialScreen.kt)
- Update `androidx.compose.ui.tooling.preview.Preview` import to `org.jetbrains.compose.ui.tooling.preview.Preview`.

## Verification Plan

### Automated Tests
- Run `./gradlew :core:ui:compileDebugKotlinAndroid` to verify the fix for the reported error.
- Run `./gradlew assembleDebug` to ensure the entire project builds correctly.

### Manual Verification
- N/A (Build fix)
