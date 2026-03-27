package com.senai.npsv_gestor_tintas_mobile.data.remote

import com.senai.npsv_gestor_tintas_mobile.data.local.TokenStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCliente {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun criarServico(tokenStore: TokenStore): ApiService {

        val interceptorAuth = Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            val caminhoDaUrl = request.url.encodedPath

            // Só adiciona o token se NÃO for a rota de login ou cadastro
            if (!caminhoDaUrl.contains("auth/login") && !caminhoDaUrl.contains("api/usuarios")) {
                val token = runBlocking { tokenStore.token.firstOrNull() }
                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
            }

            chain.proceed(requestBuilder.build())
        }

        val clienteOkHttp = OkHttpClient.Builder()
            .addInterceptor(interceptorAuth)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteOkHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}