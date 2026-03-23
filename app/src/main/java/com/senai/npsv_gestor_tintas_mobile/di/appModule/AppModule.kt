package com.senai.npsv_gestor_tintas_mobile.di.appModule

import android.content.Context
import com.senai.npsv_gestor_tintas_mobile.data.local.TokenStore
import com.senai.npsv_gestor_tintas_mobile.data.remote.ApiService
import com.senai.npsv_gestor_tintas_mobile.data.remote.RetrofitCliente
import com.senai.npsv_gestor_tintas_mobile.data.repository.AuthRepository

object AppModule {

    private var tokenStore: TokenStore? = null
    private var apiService: ApiService? = null
    private var authRepository: AuthRepository? = null


    fun provideAuthRepository(context: Context): AuthRepository {
        if (authRepository == null) {
            val store = tokenStore ?: TokenStore(context.applicationContext).also { tokenStore = it }
            val api = apiService ?: RetrofitCliente.criarServico(store).also { apiService = it }
            authRepository = AuthRepository(api, store)
        }
        return authRepository!!
    }
}