package com.senai.npsv_gestor_tintas_mobile.ui.usuarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.senai.npsv_gestor_tintas_mobile.data.remote.UsuarioResponseDTO
import com.senai.npsv_gestor_tintas_mobile.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ListaUsuariosUiState {
    object Loading : ListaUsuariosUiState()
    data class Success(val usuarios: List<UsuarioResponseDTO>) : ListaUsuariosUiState()
    data class Error(val message: String) : ListaUsuariosUiState()
}

class ListaUsuariosViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<ListaUsuariosUiState>(ListaUsuariosUiState.Loading)
    val uiState: StateFlow<ListaUsuariosUiState> = _uiState.asStateFlow()

    fun carregarUsuarios() {
        _uiState.value = ListaUsuariosUiState.Loading

        viewModelScope.launch {
            val result = repository.listarUsuarios()
            result.onSuccess { lista ->
                _uiState.value = ListaUsuariosUiState.Success(lista)
            }.onFailure { error ->
                _uiState.value = ListaUsuariosUiState.Error(error.message ?: "Erro desconhecido ao carregar utilizadores.")
            }
        }
    }

    class Factory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListaUsuariosViewModel(repository) as T
        }
    }
}