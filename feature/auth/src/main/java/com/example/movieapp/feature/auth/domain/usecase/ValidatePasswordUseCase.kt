package com.example.movieapp.feature.auth.domain.usecase

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke (password: String) : PasswordValidationResult{
        if (password.length < 8) {
            return PasswordValidationResult.Failure("auth_error_password_too_short")
        }
        if (!password.any { it.isUpperCase() }) {
            return PasswordValidationResult.Failure("auth_error_password_no_uppercase")
        }
        if (!password.any { it.isDigit() }) {
            return PasswordValidationResult.Failure("auth_error_password_no_digit")
        }
        return PasswordValidationResult.Success
    }
}
sealed class PasswordValidationResult{
    object Success : PasswordValidationResult()
    data   class Failure(val errorKey : String) : PasswordValidationResult()
}