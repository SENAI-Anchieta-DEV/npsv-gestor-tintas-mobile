package com.senai.npsv_gestor_tintas_mobile.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.senai.npsv_gestor_tintas_mobile.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, senha: String) {
        if (email.isBlank() || senha.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Preencha todos os campos") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }


        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)

            if (email == "vendedor@loja.com" && senha == "123456") {
                _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Credenciais inválidas. Tente vendedor@loja.com / 123456") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    class Factory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(authRepository) as T
        }
    }
}