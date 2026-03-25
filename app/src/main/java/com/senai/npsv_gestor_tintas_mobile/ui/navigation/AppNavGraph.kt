package com.senai.npsv_gestor_tintas_mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.senai.npsv_gestor_tintas_mobile.ui.estoque.EstoqueScreen
import com.senai.npsv_gestor_tintas_mobile.ui.login.LoginScreen
import com.senai.npsv_gestor_tintas_mobile.ui.precos.PrecosScreen
import com.senai.npsv_gestor_tintas_mobile.ui.prevendas.PreVendaScreen
import com.senai.npsv_gestor_tintas_mobile.ui.producao.ProducaoScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Routes.Estoque.route) {
                    popUpTo(Routes.Login.route) { inclusive = true }
                }
            })
        }

        composable(Routes.Estoque.route) {
            EstoqueScreen() // Certifique-se que o import está correto
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
    }
}