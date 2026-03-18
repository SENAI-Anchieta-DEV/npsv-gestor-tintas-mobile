package com.senai.npsv_gestor_tintas_mobile.core.state


sealed interface UiState<out T> {
    data object Initial : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}