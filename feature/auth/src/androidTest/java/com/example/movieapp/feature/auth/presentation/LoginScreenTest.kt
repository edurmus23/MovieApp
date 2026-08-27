package com.example.movieapp.feature.auth.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.movieapp.feature.auth.R
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun loginScreen_initialState_isCorrect() {
        composeTestRule.setContent {
            LoginContent(
                state = AuthState(),
                onLogin = { _, _ -> },
                onNavigateToRegister = {},
                onSkipClick = {}
            )
        }

        // Verify title is displayed
        composeTestRule.onNodeWithText(context.getString(R.string.auth_login_title)).assertIsDisplayed()

        // Verify email and password fields are present by checking their labels
        composeTestRule.onNodeWithText(context.getString(R.string.auth_email_label)).assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.auth_password_label)).assertIsDisplayed()

        // Verify login button is present
        composeTestRule.onNodeWithText(context.getString(R.string.auth_login_button)).assertIsDisplayed()
    }

    @Test
    fun loginScreen_inputInteraction_worksCorrectly() {
        composeTestRule.setContent {
            LoginContent(
                state = AuthState(),
                onLogin = { _, _ -> },
                onNavigateToRegister = {},
                onSkipClick = {}
            )
        }

        val testEmail = "test@example.com"
        val testPassword = "Password123"

        // Use testTag to find nodes
        composeTestRule.onNodeWithTag("email_field").performTextInput(testEmail)
        composeTestRule.onNodeWithTag("password_field").performTextInput(testPassword)
        
        // Click login button
        composeTestRule.onNodeWithTag("login_button").performClick()

        // Verify input exists in semantics
        composeTestRule.onNodeWithText(testEmail).assertExists()
    }
}
