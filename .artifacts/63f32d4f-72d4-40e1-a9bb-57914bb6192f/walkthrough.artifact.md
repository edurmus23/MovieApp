# Fix Unresolved Reference 'tooling' for Preview in KMP

I have resolved the `Unresolved reference 'tooling'` build error by updating the `@Preview` imports across the Kotlin Multiplatform (KMP) modules.

## Changes Made

### 1. Updated Imports
In KMP `commonMain` source sets, the Android-specific `androidx.compose.ui.tooling.preview.Preview` was replaced with the multiplatform-compatible `org.jetbrains.compose.ui.tooling.preview.Preview`.

Modified files:
- [SectionHeader.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/core/ui/src/commonMain/kotlin/com/example/movieapp/core/ui/components/SectionHeader.kt)
- [AiChatScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/app/ai/src/commonMain/kotlin/com/example/movieapp/ai/presentation/AiChatScreen.kt)
- [ListDetailScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/favorites/src/commonMain/kotlin/com/example/movieapp/feature/favorites/ListDetailScreen.kt)
- [SocialScreen.kt](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/social/src/commonMain/kotlin/com/example/movieapp/feature/social/presentation/social/SocialScreen.kt)

### 2. Adjusted Preview Parameters
The multiplatform `@Preview` annotation does not support `showBackground` or `backgroundColor` parameters in common code. These parameters were removed to ensure successful compilation.

## Verification Results

### Automated Tests
- Ran `:core:ui:compileDebugKotlinAndroid` successfully.
- Verified that other modules (`app:ai`, `feature:favorites`, `feature:social`) no longer report the `tooling` unresolved reference error.

> [!IMPORTANT]
> While the `tooling` error is resolved, some modules (like `app:ai`) still have other unresolved references (e.g., missing resources or icons). These appear to be pre-existing issues unrelated to the `tooling` fix.
