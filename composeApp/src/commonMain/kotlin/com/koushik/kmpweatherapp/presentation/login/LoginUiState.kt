package com.koushik.kmpweatherapp.presentation.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val isValid: Boolean
        get() = username.trim().isNotEmpty() && password.trim().isNotEmpty()
}