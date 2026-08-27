package com.example.movieapp.feature.auth.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidatePasswordUseCaseTest {

    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    @Before
    fun setUp() {
        validatePasswordUseCase = ValidatePasswordUseCase()
    }

    @Test
    fun `when password is less than 8 characters, return too short failure`() {
        val result = validatePasswordUseCase("Pass1")
        assertTrue(result is PasswordValidationResult.Failure)
        assertEquals("auth_error_password_too_short", (result as PasswordValidationResult.Failure).errorKey)
    }

    @Test
    fun `when password has no uppercase, return no uppercase failure`() {
        val result = validatePasswordUseCase("password123")
        assertTrue(result is PasswordValidationResult.Failure)
        assertEquals("auth_error_password_no_uppercase", (result as PasswordValidationResult.Failure).errorKey)
    }

    @Test
    fun `when password has no digit, return no digit failure`() {
        val result = validatePasswordUseCase("Password")
        assertTrue(result is PasswordValidationResult.Failure)
        assertEquals("auth_error_password_no_digit", (result as PasswordValidationResult.Failure).errorKey)
    }

    @Test
    fun `when password is valid, return success`() {
        val result = validatePasswordUseCase("Password123")
        assertEquals(PasswordValidationResult.Success, result)
    }
}
