package com.senai.npsv_gestor_tintas_mobile.data.remote

data class CadastroUsuarioRequest(
    val nome: String,
    val email: String,
    val senha: String,
    val role: String = "VENDEDOR"
)