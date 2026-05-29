package com.senai.npsv_gestor_tintas_mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.senai.npsv_gestor_tintas_mobile.ui.cadastro.CadastroScreen
import com.senai.npsv_gestor_tintas_mobile.ui.estoque.EstoqueScreen
import com.senai.npsv_gestor_tintas_mobile.ui.login.LoginScreen
import com.senai.npsv_gestor_tintas_mobile.ui.precos.PrecosScreen
import com.senai.npsv_gestor_tintas_mobile.ui.prevendas.PreVendaScreen
import com.senai.npsv_gestor_tintas_mobile.ui.producao.ProducaoScreen
import com.senai.npsv_gestor_tintas_mobile.ui.producao.MonitoramentoScreen
import com.senai.npsv_gestor_tintas_mobile.ui.usuarios.ListaUsuariosScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        // TELA DE LOGIN
        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Estoque.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // TELA DE ESTOQUE
        composable(Routes.Estoque.route) {
            EstoqueScreen(
                onNavigateToUsuarios = {
                    navController.navigate(Routes.ListaUsuarios.route)
                },
                onNavigateToPrecos = {
                    navController.navigate(Routes.Precos.route)
                }
            )
        }

        // TELA DE LISTA DE UTILIZADORES
        composable(Routes.ListaUsuarios.route) {
            ListaUsuariosScreen(
                onNavigateToCadastro = {
                    navController.navigate(Routes.Cadastro.route)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // TELA DE CADASTRO
        composable(Routes.Cadastro.route) {
            CadastroScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // TELA DE PREÇOS
        composable(Routes.Precos.route) {
            PrecosScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // TELA DE PRÉ-VENDA
        composable(Routes.PreVenda.route) {
            PreVendaScreen()
        }

        // TELA DE PRODUÇÃO (Agora com o botão de navegação)
        composable(Routes.Producao.route) {
            ProducaoScreen(
                onNavigateToMonitoramento = { producaoId ->
                    navController.navigate(Routes.Monitoramento.createRoute(producaoId))
                }
            )
        }

        // NOVA TELA DE MONITORAMENTO IoT
        composable(
            route = Routes.Monitoramento.route,
            arguments = listOf(navArgument("producaoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val producaoId = backStackEntry.arguments?.getString("producaoId") ?: ""

            MonitoramentoScreen(
                producaoId = producaoId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}