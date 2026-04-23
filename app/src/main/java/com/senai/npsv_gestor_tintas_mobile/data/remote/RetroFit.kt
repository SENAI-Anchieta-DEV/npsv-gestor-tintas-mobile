package com.senai.npsv_gestor_tintas_mobile.data.remote

import com.senai.npsv_gestor_tintas_mobile.data.local.TokenStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit // <-- IMPORTANTE: Importar o TimeUnit

object RetrofitCliente {


    private const val BASE_URL = "https://gestor-tintas-backend-api.onrender.com"

    fun criarServico(tokenStore: TokenStore): ApiService {

        val interceptorAuth = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            val token = runBlocking { tokenStore.token.first() }

            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

        val clienteOkHttp = OkHttpClient.Builder()
            .addInterceptor(interceptorAuth)

            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(clienteOkHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}