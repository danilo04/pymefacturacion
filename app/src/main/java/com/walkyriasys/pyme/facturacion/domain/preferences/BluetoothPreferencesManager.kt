package com.walkyriasys.pyme.facturacion.domain.preferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSelectedBluetoothDevice(address: String, name: String) {
        sharedPreferences.edit()
            .putString(SELECTED_DEVICE_ADDRESS, address)
            .putString(SELECTED_DEVICE_NAME, name)
            .apply()
    }

    fun getSelectedBluetoothDeviceAddress(): String? {
        return sharedPreferences.getString(SELECTED_DEVICE_ADDRESS, null)
    }

    fun getSelectedBluetoothDeviceName(): String? {
        return sharedPreferences.getString(SELECTED_DEVICE_NAME, null)
    }

    fun clearSelectedBluetoothDevice() {
        sharedPreferences.edit()
            .remove(SELECTED_DEVICE_ADDRESS)
            .remove(SELECTED_DEVICE_NAME)
            .apply()
    }

    fun hasSelectedDevice(): Boolean {
        return getSelectedBluetoothDeviceAddress() != null
    }

    companion object {
        private const val PREFS_NAME = "bluetooth_preferences"
        private const val SELECTED_DEVICE_ADDRESS = "selected_device_address"
        private const val SELECTED_DEVICE_NAME = "selected_device_name"
    }
}
