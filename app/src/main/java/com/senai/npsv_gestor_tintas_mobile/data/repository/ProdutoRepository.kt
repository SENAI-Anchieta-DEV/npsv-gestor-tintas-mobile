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

                // Converte DTO da API para o nosso Modelo Inteligente do App
                val listaProdutos = listaDto.map { dto ->
                    Produto(
                        id = dto.id,
                        codigoBarras = dto.codigoBarras,
                        descricao = dto.descricao,
                        quantidadeEstoque = dto.quantidadeEstoque,
                        precoCusto = dto.precoCusto,
                        precoVenda = dto.precoVenda,
                        unidadeMedida = dto.unidadeMedida,
                        categoria = dto.categoria?.nome ?: "Sem Categoria",
                        // Mapeamento essencial: Pega o valor da API ou assume 10.0 como padrão de segurança se vier nulo
                        estoqueMinimo = dto.estoqueMinimo ?: 10.0
                    )
                }
                Result.success(listaProdutos)
            } else {
                Result.failure(Exception("Erro na resposta: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("ProdutoRepository", "Erro de rede: ${e.message}")
            Result.failure(Exception("Falha de conexão ao carregar estoque."))
        }
    }
}