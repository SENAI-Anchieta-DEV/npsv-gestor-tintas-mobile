package com.senai.npsv_gestor_tintas_mobile.ui.estoque

import androidx.lifecycle.ViewModel
import com.senai.npsv_gestor_tintas_mobile.domain.model.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class CategoriaEstoque {
    TUDO, BASE, PIGMENTO, INSUMO
}

data class EstoqueUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val categoriaSelecionada: CategoriaEstoque = CategoriaEstoque.TUDO,
    val produtos: List<Produto> = emptyList()
)

class EstoqueViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EstoqueUiState())
    val uiState: StateFlow<EstoqueUiState> = _uiState.asStateFlow()


    private val produtosMock = listOf(
        Produto(1, "Base Fosca 18L", "BASE", 45.0, "un"),
        Produto(2, "Pigmento Azul Cobalto", "PIGMENTO", 2.4, "kg"),
        Produto(3, "Pigmento Amarelo Ouro", "PIGMENTO", 5.1, "kg"),
        Produto(4, "Base Acrílica Premium", "BASE", 12.0, "un"),
        Produto(5, "Resina Brilhante", "INSUMO", 15.0, "un")
    )

    init {

        _uiState.update { it.copy(produtos = produtosMock) }
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
        val filtrados = produtosMock.filter { produto ->
            val matchBusca = produto.nome.contains(estadoAtual.searchQuery, ignoreCase = true)
            val matchCategoria = if (estadoAtual.categoriaSelecionada == CategoriaEstoque.TUDO) {
                true
            } else {
                produto.categoria == estadoAtual.categoriaSelecionada.name
            }
            matchBusca && matchCategoria
        }
        _uiState.update { it.copy(produtos = filtrados) }
    }
}