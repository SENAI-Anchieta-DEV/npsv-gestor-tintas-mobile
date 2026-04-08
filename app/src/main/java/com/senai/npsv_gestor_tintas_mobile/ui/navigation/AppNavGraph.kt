package com.senai.npsv_gestor_tintas_mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.senai.npsv_gestor_tintas_mobile.data.local.TokenStore
import com.senai.npsv_gestor_tintas_mobile.data.remote.RetrofitCliente
import com.senai.npsv_gestor_tintas_mobile.data.repository.AuthRepository
import com.senai.npsv_gestor_tintas_mobile.ui.estoque.EstoqueScreen
import com.senai.npsv_gestor_tintas_mobile.ui.login.LoginScreen
import com.senai.npsv_gestor_tintas_mobile.ui.login.LoginViewModel
import com.senai.npsv_gestor_tintas_mobile.ui.precos.PrecosScreen
import com.senai.npsv_gestor_tintas_mobile.ui.prevendas.PreVendaScreen
import com.senai.npsv_gestor_tintas_mobile.ui.producao.ProducaoScreen
import com.senai.npsv_gestor_tintas_mobile.ui.usuarios.UserRegistrationScreen
import com.senai.npsv_gestor_tintas_mobile.ui.usuarios.UserRegistrationViewModel

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            val context = LocalContext.current
            val tokenStore = TokenStore(context)
            val apiService = RetrofitCliente.criarServico(tokenStore)
            val authRepository = AuthRepository(apiService, tokenStore)

            val viewModel: LoginViewModel = viewModel(
                factory = LoginViewModel.Factory(authRepository)
            )


            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.Estoque.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToCadastro = {
                    navController.navigate(Routes.Cadastro.route)
                }
            )
        }
        composable(Routes.Estoque.route) {
            EstoqueScreen()
        }

        composable(Routes.Precos.route) {
            PrecosScreen()
        }

        composable(Routes.PreVenda.route) {
            PreVendaScreen()
        }

        composable(Routes.Producao.route) {
            ProducaoScreen()
        }

        composable(Routes.Cadastro.route) {
            val apiService = RetrofitCliente.criarServico(TokenStore(LocalContext.current))
            val viewModel: UserRegistrationViewModel = viewModel(factory = UserRegistrationViewModel.Factory(apiService))


            UserRegistrationScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}