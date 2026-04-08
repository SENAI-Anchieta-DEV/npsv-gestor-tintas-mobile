package com.senai.npsv_gestor_tintas_mobile.data.remote

data class UsuarioRequestDTO(
    val nome: String,
    val email: String,
    val senha: String,
    val role: String
)

data class UsuarioResponseDTO(
    val id: String,
    val nome: String,
    val email: String,
    val role: String,
    val ativo: Boolean
)