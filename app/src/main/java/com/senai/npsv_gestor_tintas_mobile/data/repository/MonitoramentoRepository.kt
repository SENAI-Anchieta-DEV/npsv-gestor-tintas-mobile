package com.senai.npsv_gestor_tintas_mobile.data.repository

import android.util.Log
import com.senai.npsv_gestor_tintas_mobile.data.remote.ApiService
import com.senai.npsv_gestor_tintas_mobile.data.remote.PesagemAtualResponseDTO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class MonitoramentoRepository(private val apiService: ApiService) {

    fun monitorarPesagemEmTempoReal(producaoId: String): Flow<Result<PesagemAtualResponseDTO>> = flow {
        while (true) {
            try {
                val response = apiService.getPesagemAtual(producaoId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        emit(Result.success(it))
                    } ?: emit(Result.failure(Exception("Corpo da resposta vazio.")))
                } else {
                    emit(Result.failure(Exception("Erro HTTP: ${response.code()}")))
                }
            } catch (e: IOException) {
                emit(Result.failure(Exception("Sem conexão com o servidor ou balança.")))
            } catch (e: HttpException) {
                emit(Result.failure(Exception("Erro de rede inesperado.")))
            } catch (e: Exception) {
                Log.e("Monitoramento", "Erro no polling", e)
                emit(Result.failure(e))
            }

            delay(500)
        }
    }
}