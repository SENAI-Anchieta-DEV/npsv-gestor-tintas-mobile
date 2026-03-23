package com.senai.npsv_gestor_tintas_mobile.domain.model

data class Produto(
    val id: Int,
    val nome: String,
    val categoria: String,
    val quantidade: Double,
    val unidadeMedida: String
)