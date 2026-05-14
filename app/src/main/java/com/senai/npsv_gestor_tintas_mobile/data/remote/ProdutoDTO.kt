package com.senai.npsv_gestor_tintas_mobile.data.remote

data class CategoriaProdutoDTO(
    val id: String? = null,
    val nome: String,
    val descricao: String? = null
)


data class ProdutoResponseDTO(
    val id: String,
    val codigoBarras: String,
    val descricao: String,
    val quantidadeEstoque: Double,
    val precoCusto: Double,
    val precoVenda: Double,
    val unidadeMedida: String,
    val categoria: CategoriaProdutoDTO,
    val estoqueMinimo: Double
)

