package com.senai.npsv_gestor_tintas_mobile.domain.model

enum class NivelEstoque {
    BAIXO, MEDIO, ALTO
}

data class Produto(
    val id: String,
    val codigoBarras: String,
    val descricao: String,
    val quantidadeEstoque: Double,
    val precoCusto: Double,
    val precoVenda: Double,
    val unidadeMedida: String,
    val categoria: String
) {
    val nivelEstoque: NivelEstoque
        get() = when {
            quantidadeEstoque <= 10.0 -> NivelEstoque.BAIXO
            quantidadeEstoque <= 50.0 -> NivelEstoque.MEDIO
            else -> NivelEstoque.ALTO
        }
}