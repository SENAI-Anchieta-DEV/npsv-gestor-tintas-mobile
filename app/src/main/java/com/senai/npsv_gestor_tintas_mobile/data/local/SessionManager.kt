package com.senai.npsv_gestor_tintas_mobile.data.local

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SessionManager {
    private val _sessionExpiredEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)

    val sessionExpiredEvent = _sessionExpiredEvent.asSharedFlow()

    fun emitSessionExpired() {
        _sessionExpiredEvent.tryEmit(Unit)
    }
}