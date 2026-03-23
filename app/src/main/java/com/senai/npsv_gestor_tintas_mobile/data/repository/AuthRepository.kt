package com.senai.npsv_gestor_tintas_mobile.data.repository

import com.senai.npsv_gestor_tintas_mobile.data.local.TokenStore
import com.senai.npsv_gestor_tintas_mobile.data.remote.ApiService
import com.senai.npsv_gestor_tintas_mobile.data.remote.LoginRequest

class AuthRepository(
    private val apiService: ApiService,
    private val tokenStore: TokenStore
) {
    suspend fun login(email: String, senha: String): Result<Unit> {
        return try {

            val response = apiService.login(LoginRequest(email, senha))

            if (response.isSuccessful && response.body() != null) {

                val token = response.body()!!.token
                tokenStore.saveToken(token)
                Result.success(Unit)
            } else {

                Result.failure(Exception("Falha no login. Verifique suas credenciais."))
            }
        } catch (e: Exception) {

            Result.failure(Exception("Erro de conexão: ${e.message}"))
        }
    }

    suspend fun logout() {
        tokenStore.clearToken()
    }
}