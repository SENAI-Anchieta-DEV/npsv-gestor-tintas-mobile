package com.senai.npsv_gestor_tintas_mobile.data.repository

import android.util.Log
import com.senai.npsv_gestor_tintas_mobile.data.remote.ApiService
import com.senai.npsv_gestor_tintas_mobile.domain.model.Produto

class ProdutoRepository(private val apiService: ApiService) {

    suspend fun listarProdutos(): Result<List<Produto>> {
        return try {
            val response = apiService.listarProdutos()
            if (response.isSuccessful) {
                val listaDto = response.body() ?: emptyList()

                // Converte o DTO da API para o nosso Modelo do App
                val listaProdutos = listaDto.map { dto ->
                    Produto(
                        id = dto.id,
                        descricao = dto.descricao,
                        categoria = dto.categoria?.nome ?: "Sem Categoria",
                        quantidadeEstoque = dto.quantidadeEstoque,
                        unidadeMedida = dto.unidadeMedida,
                        // Prevenção: caso o backend não envie, assume 10.0 como alerta padrão
                        estoqueMinimo = dto.estoqueMinimo ?: 10.0
                    )
                }
                Result.success(listaProdutos)
            } else {
                Result.failure(Exception("Erro na resposta do servidor: Código ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("ProdutoRepository", "Erro de rede: ${e.message}")
            Result.failure(Exception("Falha de conexão. Verifique a rede."))
        }
    }
}