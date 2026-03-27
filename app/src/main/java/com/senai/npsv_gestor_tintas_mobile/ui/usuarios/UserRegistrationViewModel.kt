package com.senai.npsv_gestor_tintas_mobile.ui.usuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.senai.npsv_gestor_tintas_mobile.data.remote.ApiService
import com.senai.npsv_gestor_tintas_mobile.data.remote.CadastroUsuarioRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserRegistrationViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uiState = MutableStateFlow(UserRegistrationUiState())
    val uiState: StateFlow<UserRegistrationUiState> = _uiState.asStateFlow()

    fun cadastrarUsuario(nome: String, email: String, senha: String) {

        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            _uiState.update { it.copy(validationError = "Por favor, preencha todos os campos obrigatórios.") }
            return
        }


        _uiState.update { it.copy(isLoading = true, validationError = null, errorMessage = null) }

        viewModelScope.launch {
            try {
                val request = CadastroUsuarioRequest(nome, email, senha)
                val response = apiService.cadastrarUsuario(request)

                if (response.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = extractErrorMessage(errorBody) ?: "Erro HTTP: ${response.code()}"
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Falha de conexão: ${e.message}") }
            }
        }
    }

    private fun extractErrorMessage(errorBody: String?): String? {
        return try {
            if (!errorBody.isNullOrEmpty()) {
                JSONObject(errorBody).getString("detail")
            } else null
        } catch (e: Exception) {
            null
        }
    }

    fun resetState() {
        _uiState.update { UserRegistrationUiState() }
    }

    class Factory(private val apiService: ApiService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserRegistrationViewModel(apiService) as T
        }
    }
}