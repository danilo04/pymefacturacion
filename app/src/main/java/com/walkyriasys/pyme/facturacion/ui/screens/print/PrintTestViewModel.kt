package com.walkyriasys.pyme.facturacion.ui.screens.print

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkyriasys.pyme.facturacion.domain.preferences.BluetoothPreferencesManager
import com.walkyriasys.pyme.facturacion.domain.print.BluetoothConnectionManager
import com.walkyriasys.pyme.facturacion.domain.print.PrinterService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrintTestViewModel @Inject constructor(
    private val printerService: PrinterService,
    private val bluetoothConnectionManager: BluetoothConnectionManager,
    private val bluetoothPreferencesManager: BluetoothPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrintTestUiState())
    val uiState: StateFlow<PrintTestUiState> = _uiState.asStateFlow()

    init {
        checkConnectionStatus()
    }

    private fun checkConnectionStatus() {
        viewModelScope.launch {
            val isConnected = bluetoothConnectionManager.isConnected()
            val hasDevice = bluetoothPreferencesManager.hasSelectedDevice()
            val deviceName = bluetoothPreferencesManager.getSelectedBluetoothDeviceName()
            
            _uiState.value = _uiState.value.copy(
                isConnected = isConnected,
                hasSelectedDevice = hasDevice,
                selectedDeviceName = deviceName
            )
        }
    }

    fun printTestDocument() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val result = printerService.printTestDocument()
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "Test document printed successfully!"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "Failed to print: ${result.exceptionOrNull()?.message}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Error printing document: ${e.message}"
                )
            }
        }
    }

    fun printQRCode(data: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val result = printerService.printQRCode(data)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "QR Code printed successfully!"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "Failed to print QR Code: ${result.exceptionOrNull()?.message}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Error printing QR Code: ${e.message}"
                )
            }
        }
    }

    fun printBarcode(data: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val result = printerService.printBarcode(data)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "Barcode printed successfully!"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "Failed to print barcode: ${result.exceptionOrNull()?.message}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Error printing barcode: ${e.message}"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun refreshConnectionStatus() {
        checkConnectionStatus()
    }

    fun connect() {
        viewModelScope.launch {
            val savedAddress = bluetoothPreferencesManager.getSelectedBluetoothDeviceAddress()
            if (savedAddress != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, message = "Connecting to printer...")
                
                try {
                    val result = bluetoothConnectionManager.connect(savedAddress)
                    when (result) {
                        is BluetoothConnectionManager.BluetoothConnectionResult.Connected -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isConnected = true,
                                message = "Successfully connected to printer!"
                            )
                        }
                        is BluetoothConnectionManager.BluetoothConnectionResult.BluetoothDisabled -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                message = "Bluetooth is disabled. Please enable Bluetooth."
                            )
                        }
                        is BluetoothConnectionManager.BluetoothConnectionResult.InvalidAddress -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                message = "Invalid device address."
                            )
                        }
                        is BluetoothConnectionManager.BluetoothConnectionResult.Connecting -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                message = "Already connecting..."
                            )
                        }
                        is BluetoothConnectionManager.BluetoothConnectionResult.ErrorConnecting -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                message = "Failed to connect: ${result.exception?.message ?: "Unknown error"}"
                            )
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        message = "Connection error: ${e.message}"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    message = "No saved printer found. Please go to Settings to select a printer first."
                )
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = "Disconnecting...")
            
            try {
                bluetoothConnectionManager.disconnect()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isConnected = false,
                    message = "Disconnected from printer"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Error disconnecting: ${e.message}"
                )
            }
        }
    }
}

data class PrintTestUiState(
    val isConnected: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null,
    val hasSelectedDevice: Boolean = false,
    val selectedDeviceName: String? = null
)
