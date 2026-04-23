package com.senai.npsv_gestor_tintas_mobile.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val email: String, val senha: String)
data class AuthResponse(val token: String)

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/usuarios")
    suspend fun registrarUsuario(@Body request: UsuarioRequestDTO): Response<UsuarioResponseDTO>


    @POST("api/usuarios")
    suspend fun cadastrarUsuario(@Body request: CadastroUsuarioRequest): Response<Unit>

    @GET("api/usuarios")
    suspend fun listarUsuarios(): Response<List<UsuarioResponseDTO>>
}
