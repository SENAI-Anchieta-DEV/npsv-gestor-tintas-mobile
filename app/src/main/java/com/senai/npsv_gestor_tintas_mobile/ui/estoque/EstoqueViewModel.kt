package com.senai.npsv_gestor_tintas_mobile.ui.estoque

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

enum class CategoriaEstoque {
    TUDO, BASE, PIGMENTO, INSUMO
}

data class EstoqueUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val categoriaSelecionada: CategoriaEstoque = CategoriaEstoque.TUDO,
    val produtosOriginais: List<Produto> = emptyList(),
    val produtosExibidos: List<Produto> = emptyList()
)

class EstoqueViewModel(private val repository: ProdutoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EstoqueUiState())
    val uiState: StateFlow<EstoqueUiState> = _uiState.asStateFlow()

    init {
        carregarEstoque()
    }

    fun carregarEstoque() {
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
                    aplicarFiltros() // Re-aplica filtros caso algo já estivesse digitado
                }
                .onFailure { erro ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = erro.message)
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        aplicarFiltros()
    }

    fun onCategoriaSelected(categoria: CategoriaEstoque) {
        _uiState.update { it.copy(categoriaSelecionada = categoria) }
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val estadoAtual = _uiState.value
        val filtrados = estadoAtual.produtosOriginais.filter { produto ->
            // Filtro por Nome/Descrição (Ignorando maiúsculas e minúsculas)
            val matchBusca = produto.descricao.contains(estadoAtual.searchQuery, ignoreCase = true)

            // Filtro por Categoria
            val matchCategoria = if (estadoAtual.categoriaSelecionada == CategoriaEstoque.TUDO) {
                true
            } else {
                produto.categoria.contains(estadoAtual.categoriaSelecionada.name, ignoreCase = true)
            }

            matchBusca && matchCategoria
        }
        _uiState.update { it.copy(produtosExibidos = filtrados) }
    }

    // Factory para Injeção do Repositório
    class Factory(private val repository: ProdutoRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EstoqueViewModel(repository) as T
        }
    }
}