package com.senai.npsv_gestor_tintas_mobile.ui.precos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.senai.npsv_gestor_tintas_mobile.data.repository.ProdutoRepository
import com.senai.npsv_gestor_tintas_mobile.domain.model.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PrecosUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val produtosOriginais: List<Produto> = emptyList(),
    val produtosExibidos: List<Produto> = emptyList()
)

class PrecosViewModel(private val repository: ProdutoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PrecosUiState())
    val uiState: StateFlow<PrecosUiState> = _uiState.asStateFlow()

    init {
        carregarTabelaPrecos()
    }

    fun carregarTabelaPrecos() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            repository.listarProdutos()
                .onSuccess { lista ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            produtosOriginais = lista,
                            produtosExibidos = lista
                        )
                    }
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = erro.message ?: "Erro ao carregar a tabela de preços."
                        )
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        aplicarFiltroLocal()
    }

    private fun aplicarFiltroLocal() {
        val state = _uiState.value
        val filtrados = if (state.searchQuery.isBlank()) {
            state.produtosOriginais
        } else {
            state.produtosOriginais.filter {
                it.descricao.contains(state.searchQuery, ignoreCase = true) ||
                        it.codigoBarras.contains(state.searchQuery, ignoreCase = true)
            }
        }
        _uiState.update { it.copy(produtosExibidos = filtrados) }
    }

    class Factory(private val repository: ProdutoRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PrecosViewModel(repository) as T
        }
    }
}