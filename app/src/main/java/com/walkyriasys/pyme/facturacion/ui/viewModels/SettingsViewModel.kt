package com.walkyriasys.pyme.facturacion.ui.viewModels

import androidx.lifecycle.ViewModel
import com.walkyriasys.pyme.facturacion.domain.preferences.BluetoothPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val bluetoothPreferencesManager: BluetoothPreferencesManager
) : ViewModel() {

    private val _selectedPrinterName = MutableStateFlow<String?>(null)
    val selectedPrinterName: StateFlow<String?> = _selectedPrinterName.asStateFlow()

    init {
        loadSelectedPrinter()
    }

    private fun loadSelectedPrinter() {
        _selectedPrinterName.value = bluetoothPreferencesManager.getSelectedBluetoothDeviceName()
    }

    fun refreshSelectedPrinter() {
        loadSelectedPrinter()
    }
}
