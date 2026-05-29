package com.senai.npsv_gestor_tintas_mobile.ui.producao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.senai.npsv_gestor_tintas_mobile.data.remote.PesagemAtualResponseDTO
import com.senai.npsv_gestor_tintas_mobile.data.repository.MonitoramentoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class MonitoramentoUiState {
    object Loading : MonitoramentoUiState()
    data class Success(val pesagem: PesagemAtualResponseDTO) : MonitoramentoUiState()
    data class Error(val message: String) : MonitoramentoUiState()
}

class MonitoramentoViewModel(private val repository: MonitoramentoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<MonitoramentoUiState>(MonitoramentoUiState.Loading)
    val uiState: StateFlow<MonitoramentoUiState> = _uiState.asStateFlow()

    private var monitoramentoJob: Job? = null


    fun iniciarMonitoramento(producaoId: String) {
          monitoramentoJob?.cancel()
        _uiState.value = MonitoramentoUiState.Loading

        monitoramentoJob = viewModelScope.launch {
            repository.monitorarPesagemEmTempoReal(producaoId).collect { result ->
                result.onSuccess { dados ->
                    _uiState.update { MonitoramentoUiState.Success(dados) }
                }.onFailure { erro ->
                    _uiState.update { MonitoramentoUiState.Error(erro.message ?: "Conexão perdida.") }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        monitoramentoJob?.cancel()
    }

    class Factory(private val repository: MonitoramentoRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MonitoramentoViewModel(repository) as T
        }
    }
}