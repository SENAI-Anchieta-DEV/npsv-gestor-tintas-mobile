package com.senai.npsv_gestor_tintas_mobile.domain.model

data class Produto(
    val id: String,
    val descricao: String,
    val categoria: String,
    val quantidadeEstoque: Double,
    val unidadeMedida: String,
    val estoqueMinimo: Double // Campo crucial para a RF08
) {

    val isEstoqueBaixo: Boolean
        get() = quantidadeEstoque <= estoqueMinimo
}