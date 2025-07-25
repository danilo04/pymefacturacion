package com.walkyriasys.pyme.facturacion.domain.models

data class BluetoothDevice(
    val name: String,
    val address: String,
    val isConnected: Boolean = false
)
