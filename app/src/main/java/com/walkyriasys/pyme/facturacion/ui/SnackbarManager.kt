package com.walkyriasys.pyme.facturacion.ui

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackbarManager {
    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()

    suspend fun showMessage(message: String) {
        _messages.emit(message)
    }
}