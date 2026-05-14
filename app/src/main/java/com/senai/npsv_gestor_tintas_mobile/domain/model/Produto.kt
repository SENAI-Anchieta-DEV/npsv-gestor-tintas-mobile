package com.senai.npsv_gestor_tintas_mobile.domain.model

enum class NivelEstoque {
    BAIXO, ALTO
}

data class Produto(
    val id: String,
    val codigoBarras: String,
    val descricao: String,
    val quantidadeEstoque: Double,
    val precoCusto: Double,
    val precoVenda: Double,
    val unidadeMedida: String,
    val categoria: String,
    val estoqueMinimo: Double // Variável que vem da API
) {
    val nivelEstoque: NivelEstoque
        get() = if (quantidadeEstoque <= estoqueMinimo) NivelEstoque.BAIXO else NivelEstoque.ALTO
}