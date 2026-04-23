package com.senai.npsv_gestor_tintas_mobile.ui.usuarios

data class UserRegistrationUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val validationError: String? = null
)