package com.senai.npsv_gestor_tintas_mobile.ui.cadastro

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.senai.npsv_gestor_tintas_mobile.data.remote.UsuarioRequestDTO
import com.senai.npsv_gestor_tintas_mobile.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class CadastroUiState {
    object Idle : CadastroUiState()
    object Loading : CadastroUiState()
    object Success : CadastroUiState()
    data class Error(val message: String) : CadastroUiState()
}

data class CadastroFormState(
    val nomeError: String? = null,
    val emailError: String? = null,
    val senhaError: String? = null
)

class CadastroViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<CadastroUiState>(CadastroUiState.Idle)
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(CadastroFormState())
    val formState: StateFlow<CadastroFormState> = _formState.asStateFlow()

    fun cadastrar(nome: String, email: String, senha: String, role: String) {
        if (!validarCampos(nome, email, senha)) return

        _uiState.value = CadastroUiState.Loading

        viewModelScope.launch {
            val result = repository.registrarUsuario(UsuarioRequestDTO(nome, email, senha, role))
            result.onSuccess {
                _uiState.value = CadastroUiState.Success
            }.onFailure { error ->
                _uiState.value = CadastroUiState.Error(error.message ?: "Falha ao registar.")
            }
        }
    }

    private fun validarCampos(nome: String, email: String, senha: String): Boolean {
        var isValid = true
        var nomeErr: String? = null
        var emailErr: String? = null
        var senhaErr: String? = null

        if (nome.trim().isBlank()) {
            nomeErr = "O nome é obrigatório."
            isValid = false
        }

        if (email.trim().isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailErr = "Insira um e-mail válido."
            isValid = false
        }

        if (senha.isBlank() || senha.length < 6) {
            senhaErr = "A senha deve ter pelo menos 6 caracteres."
            isValid = false
        }

        _formState.update { it.copy(nomeError = nomeErr, emailError = emailErr, senhaError = senhaErr) }
        return isValid
    }

    fun resetState() {
        _uiState.value = CadastroUiState.Idle
    }

    class Factory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CadastroViewModel(repository) as T
        }
    }
}