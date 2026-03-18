package com.senai.npsv_gestor_tintas_mobile.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.senai.npsv_gestor_tintas_mobile.feature.auth.presentation.LoginScreen
import com.senai.npsv_gestor_tintas_mobile.feature.auth.presentation.LoginViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login,
        modifier = modifier
    ) {
        composable<Routes.Login> {

            val loginViewModel: LoginViewModel = viewModel()

            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToHome = {

                    navController.navigate(Routes.ConsultaEstoque) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.ConsultaEstoque> {
            // TODO: Criaremos o EstoqueScreen
        }


        composable<Routes.ConsultaPrecos> {}
        composable<Routes.NovaPreVenda> {}
        composable<Routes.AcompanhamentoProducao> {}
    }
}