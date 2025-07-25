package com.walkyriasys.pyme.facturacion.domain.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.annotation.RequiresPermission
import com.walkyriasys.pyme.facturacion.domain.models.BluetoothDevice
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BluetoothDeviceScanner @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    //@RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT])
    fun getPairedDevices(): Flow<List<BluetoothDevice>> = flow {
        val pairedDevices = bluetoothAdapter?.bondedDevices?.map { device ->
            BluetoothDevice(
                name = device.name ?: "Unknown Device",
                address = device.address,
                isConnected = false
            )
        } ?: emptyList()
        
        emit(pairedDevices)
    }

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    fun isBluetoothSupported(): Boolean {
        return bluetoothAdapter != null
    }
}
