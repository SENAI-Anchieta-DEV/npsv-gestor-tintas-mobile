package com.senai.npsv_gestor_tintas_mobile.data.repository
import android.util.Log
import com.senai.npsv_gestor_tintas_mobile.data.local.TokenStore
import com.senai.npsv_gestor_tintas_mobile.data.remote.ApiService
import com.senai.npsv_gestor_tintas_mobile.data.remote.LoginRequest
import org.json.JSONObject

class AuthRepository(
    private val apiService: ApiService,
    private val tokenStore: TokenStore
) {
    suspend fun login(email: String, senha: String): Result<Unit> {
        return try {

            Log.d("API_NETWORK", "Iniciando chamada HTTP POST para /login com $email")


            val response = apiService.login(LoginRequest(email, senha))

            if (response.isSuccessful) {

                val token = response.body()?.token

                if (token != null) {
                    tokenStore.saveToken(token)
                    Log.d("API_NETWORK", "Sucesso! Código 200. Token guardado.")
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Token não recebido do servidor."))
                }
            } else {

                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    if (!errorBody.isNullOrEmpty()) {
                        val jsonObject = JSONObject(errorBody)
                        jsonObject.getString("detail")
                    } else {
                        "Erro de autenticação (Código: ${response.code()})"
                    }
                } catch (jsonException: Exception) {
                    "Credenciais inválidas ou erro no servidor."
                }

                Log.e("API_NETWORK", "Erro HTTP ${response.code()}: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {

            Log.e("API_NETWORK", "Falha na rede: ${e.message}")
            Result.failure(Exception("Falha de conexão. Verifique se o servidor Spring Boot está rodando em 10.0.2.2:8080"))
        }
    }
}