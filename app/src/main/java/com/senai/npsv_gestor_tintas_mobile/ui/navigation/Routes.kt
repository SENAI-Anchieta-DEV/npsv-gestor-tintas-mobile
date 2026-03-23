package com.senai.npsv_gestor_tintas_mobile.ui.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Estoque : Routes("estoque")
    object Precos : Routes("precos")
    object PreVenda : Routes("pre_venda")
    object Producao : Routes("producao")
}