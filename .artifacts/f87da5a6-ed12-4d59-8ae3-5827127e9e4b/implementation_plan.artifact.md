# Implementation Plan - Fix Render Issue in SearchScreen Preview

## Goal Description
Fix the `java.lang.ClassNotFoundException: androidx.compose.ui.tooling.ComposeViewAdapter` error in the Compose Preview for `SearchScreen.kt`. This error is caused by the missing `debugImplementation(libs.androidx.compose.ui.tooling)` dependency in the `:feature:search` module.

## User Review Required
> [!NOTE]
> This is a standard dependency issue where Compose Tooling is required in every module that contains Previews to allow the IDE to host them.

## Proposed Changes

### Feature: Search

#### [MODIFY] [build.gradle.kts](file:///Users/elif.durmus/AndroidStudioProjects/MovieApp/feature/search/build.gradle.kts)
- Add `debugImplementation(libs.androidx.compose.ui.tooling)` to the dependencies block.
- Add `debugImplementation(libs.androidx.compose.ui.test.manifest)` for consistency with best practices (optional but recommended).

## Verification Plan

### Automated Tests
- `gradle_sync` to ensure the new dependency is picked up.
- `render_compose_preview` for `SearchScreenPreview` in `SearchScreen.kt`.

### Manual Verification
- The user can confirm the preview renders in the IDE.
