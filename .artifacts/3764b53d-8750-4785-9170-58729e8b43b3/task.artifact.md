# Root-Level Modularization Task List

- `[/]` Clean up and setup root modules
    - `[x]` Remove `:core:network` from `settings.gradle.kts`
    - `[x]` Add `:network` and `:navigation` to `settings.gradle.kts`
- `[ ]` Setup `:network` module
    - `[ ]` Create `/network/build.gradle.kts`
    - `[ ]` Create `AndroidManifest.xml` for `:network`
    - `[ ]` Move `MovieApiService.kt` and update its package
    - `[ ]` Create `NetworkModule.kt` in `:network`
- `[ ]` Setup `:navigation` module
    - `[ ]` Create `/navigation/build.gradle.kts`
    - `[ ]` Create `AndroidManifest.xml` for `:navigation`
    - `[ ]` Move `Navigator.kt`, `NavKey.kt`, and `BottomNavItem.kt`
- `[ ]` Update Dependencies and Fix Imports
    - `[ ]` Update all `build.gradle.kts` files
    - `[ ]` Fix all broken imports project-wide
- `[ ]` Final Verification
