package com.senai.npsv_gestor_tintas_mobile.ui.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Estoque : Routes("estoque")
    object ListaUsuarios : Routes("lista_usuarios")
    object Cadastro : Routes("cadastro")
    object Precos : Routes("precos")
    object PreVenda : Routes("prevenda")
    object Producao : Routes("producao")


    object Monitoramento : Routes("monitoramento/{producaoId}") {
        fun createRoute(producaoId: String) = "monitoramento/$producaoId"
    }
}