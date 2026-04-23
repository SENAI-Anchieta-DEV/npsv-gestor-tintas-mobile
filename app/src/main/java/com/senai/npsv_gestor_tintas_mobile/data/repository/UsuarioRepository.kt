package com.senai.npsv_gestor_tintas_mobile.data.repository

import android.util.Log
import com.senai.npsv_gestor_tintas_mobile.data.remote.ApiService
import com.senai.npsv_gestor_tintas_mobile.data.remote.UsuarioRequestDTO
import com.senai.npsv_gestor_tintas_mobile.data.remote.UsuarioResponseDTO
import org.json.JSONObject

class UsuarioRepository(private val apiService: ApiService) {

    suspend fun registrarUsuario(usuario: UsuarioRequestDTO): Result<Unit> {
        return try {
            val response = apiService.registrarUsuario(usuario)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = extrairMensagemDeErro(errorBody, response.code())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Falha na requisição: ${e.message}")
            Result.failure(Exception("Falha de conexão. Verifique a sua internet."))
        }
    }

    suspend fun listarUsuarios(): Result<List<UsuarioResponseDTO>> {
        return try {
            val response = apiService.listarUsuarios()
            if (response.isSuccessful) {

                Result.success(response.body() ?: emptyList())
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = extrairMensagemDeErro(errorBody, response.code())
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Falha de conexão ao carregar a lista de utilizadores."))
        }
    }

    private fun extrairMensagemDeErro(errorBody: String?, httpCode: Int): String {
        if (errorBody.isNullOrEmpty()) return "Erro do servidor (Código: $httpCode)"

        return try {
            val jsonObject = JSONObject(errorBody)

            if (jsonObject.has("errors")) {
                val errorsObj = jsonObject.getJSONObject("errors")
                val primeiraChave = errorsObj.keys().next()
                errorsObj.getJSONArray(primeiraChave).getString(0)
            } else {

                jsonObject.optString("detail", "Ocorreu um erro ao realizar o registo.")
            }
        } catch (e: Exception) {
            "Erro ao processar a resposta do servidor."
        }
    }
}