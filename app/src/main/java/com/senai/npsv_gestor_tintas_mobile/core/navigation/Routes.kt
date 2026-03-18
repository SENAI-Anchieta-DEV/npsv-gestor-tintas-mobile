package com.senai.npsv_gestor_tintas_mobile.core.navigation

import kotlinx.serialization.Serializable
sealed class Routes {
    @Serializable
    data object Login : Routes()

    @Serializable
    data object ConsultaEstoque : Routes()

    @Serializable
    data object ConsultaPrecos : Routes()

    @Serializable
    data object NovaPreVenda : Routes()

    @Serializable
    data object AcompanhamentoProducao : Routes()
}