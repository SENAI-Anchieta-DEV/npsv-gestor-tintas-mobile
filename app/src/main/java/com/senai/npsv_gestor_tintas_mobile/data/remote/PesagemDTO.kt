package com.senai.npsv_gestor_tintas_mobile.data.remote

data class PesagemAtualResponseDTO(
    val pesoAlvo: Double,
    val pesoLido: Double,
    val resultadoRN01: String?,
    val statusProducao: String?
)