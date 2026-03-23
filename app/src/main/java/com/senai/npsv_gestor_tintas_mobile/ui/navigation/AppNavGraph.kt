package com.senai.npsv_gestor_tintas_mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.senai.npsv_gestor_tintas_mobile.ui.login.LoginScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Routes.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.Login.route) {
            LoginScreen(

                onLoginSuccess = {
                    navController.navigate(Routes.Estoque.route) {

                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Estoque.route) {
            // TODO: Inserir EstoqueScreen
        }

        composable(Routes.Precos.route) {
            // TODO: Inserir PrecosScreen
        }

        composable(Routes.PreVenda.route) {
            // TODO: Inserir PreVendaScreen
        }

        composable(Routes.Producao.route) {
            // TODO: Inserir ProducaoScreen
        }
    }
}