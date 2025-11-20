package com.koushik.kmpweatherapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChanged(new: String) {
        _uiState.value = _uiState.value.copy(username = new, errorMessage = null)
    }

    fun onPasswordChanged(new: String) {
        _uiState.value = _uiState.value.copy(password = new, errorMessage = null)
    }

    fun login() {
        if (_uiState.value.isLoading) return

        if (!_uiState.value.isValid) {
            _uiState.value = _uiState.value.copy(errorMessage = "Enter username & password")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                delay(1500)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = false,
                    errorMessage = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun consumeLoginSuccess() {
        _uiState.value = _uiState.value.copy(loginSuccess = false)
    }
}