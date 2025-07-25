package com.walkyriasys.pyme.facturacion.ui.viewModels

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkyriasys.pyme.facturacion.domain.bluetooth.BluetoothDeviceScanner
import com.walkyriasys.pyme.facturacion.domain.models.BluetoothDevice
import com.walkyriasys.pyme.facturacion.domain.preferences.BluetoothPreferencesManager
import com.walkyriasys.pyme.facturacion.navigation.Navigator
import com.walkyriasys.pyme.facturacion.ui.SnackbarManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = BluetoothDeviceSelectionViewModel.Factory::class)
class BluetoothDeviceSelectionViewModel @AssistedInject constructor(
    private val bluetoothDeviceScanner: BluetoothDeviceScanner,
    private val bluetoothPreferencesManager: BluetoothPreferencesManager,
    @Assisted("navigator") private val navigator: Navigator,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("navigator") navigator: Navigator,
        ): BluetoothDeviceSelectionViewModel
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _selectedDevice = MutableStateFlow<BluetoothDevice?>(null)
    val selectedDevice: StateFlow<BluetoothDevice?> = _selectedDevice.asStateFlow()

    init {
        loadSelectedDevice()
        loadPairedDevices()
    }

    private fun loadSelectedDevice() {
        val savedAddress = bluetoothPreferencesManager.getSelectedBluetoothDeviceAddress()
        val savedName = bluetoothPreferencesManager.getSelectedBluetoothDeviceName()
        
        if (savedAddress != null && savedName != null) {
            _selectedDevice.value = BluetoothDevice(
                name = savedName,
                address = savedAddress,
                isConnected = false
            )
        }
    }

    //@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun loadPairedDevices() {
        viewModelScope.launch {
            if (!bluetoothDeviceScanner.isBluetoothSupported()) {
                _uiState.value = UiState.BluetoothNotSupported
                return@launch
            }

            if (!bluetoothDeviceScanner.isBluetoothEnabled()) {
                _uiState.value = UiState.BluetoothDisabled
                return@launch
            }

            bluetoothDeviceScanner.getPairedDevices()
                .catch { exception ->
                    _uiState.value = UiState.Error(exception.message ?: "Unknown error")
                }
                .collect { devices ->
                    _uiState.value = if (devices.isEmpty()) {
                        UiState.NoPairedDevices
                    } else {
                        UiState.DevicesLoaded(devices)
                    }
                }
        }
    }

    fun selectDevice(device: BluetoothDevice) {
        bluetoothPreferencesManager.saveSelectedBluetoothDevice(device.address, device.name)
        _selectedDevice.value = device
        
        viewModelScope.launch {
            SnackbarManager.showMessage("Printer ${device.name} selected successfully")
            navigator.goBack()
        }
    }

    fun clearSelectedDevice() {
        bluetoothPreferencesManager.clearSelectedBluetoothDevice()
        _selectedDevice.value = null
        
        viewModelScope.launch {
            SnackbarManager.showMessage("Printer selection cleared")
        }
    }

    //@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun refreshDevices() {
        loadPairedDevices()
    }

    sealed interface UiState {
        data object Loading : UiState
        data object BluetoothNotSupported : UiState
        data object BluetoothDisabled : UiState
        data object NoPairedDevices : UiState
        data class DevicesLoaded(val devices: List<BluetoothDevice>) : UiState
        data class Error(val message: String) : UiState
    }
}
