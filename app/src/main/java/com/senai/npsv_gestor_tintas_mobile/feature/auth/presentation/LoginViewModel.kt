package com.senai.npsv_gestor_tintas_mobile.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senai.npsv_gestor_tintas_mobile.core.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {


    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()


    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()


    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Initial)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _loginState.value = UiState.Error("Campos em branco")
            return
        }

        viewModelScope.launch {
            _loginState.value = UiState.Loading


            delay(2000)


            _loginState.value = UiState.Success(Unit)
        }
    }
}