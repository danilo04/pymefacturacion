package com.walkyriasys.pyme.facturacion.domain.print

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import com.dayoneapp.dayone.di.IOThreadDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BluetoothConnectionManager @Inject constructor(
    @ApplicationContext context: Context,
    @IOThreadDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private var inStream: InputStream? = null
    private var outStream: OutputStream? = null
    private var socket: BluetoothSocket? = null

    private var status = BluetoothConnectionStatus.DISCONNECTED
    private val statusMutex = Mutex()

    /**
     * Safely changes the connection status using a mutex to prevent race conditions
     */
    private suspend fun setStatus(newStatus: BluetoothConnectionStatus) {
        statusMutex.withLock {
            status = newStatus
        }
    }

//    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend fun connect(address: String): BluetoothConnectionResult = withContext(ioDispatcher) {
        val adapter = bluetoothManager.adapter

        if (!adapter.isEnabled) {
            return@withContext BluetoothConnectionResult.BluetoothDisabled
        }

        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            return@withContext BluetoothConnectionResult.InvalidAddress
        }

        // Check status using the mutex to ensure thread safety
        statusMutex.withLock {
            if (status == BluetoothConnectionStatus.CONNECTING) {
                return@withContext BluetoothConnectionResult.Connecting
            }
        }

        var attempts = 0
        var connected = false
        var lastException: IOException? = null

        while (attempts < MAX_ATTEMPTS && !connected) {
            attempts += 1
            try {
                val device = adapter.getRemoteDevice(address)
                socket = device.createRfcommSocketToServiceRecord(MY_UUID)

                setStatus(BluetoothConnectionStatus.CONNECTING)

                socket?.connect()

                inStream = socket?.inputStream
                outStream = socket?.outputStream
                connected = true
            } catch (e: IOException) {
                lastException = e
                // Log
                if (attempts < MAX_ATTEMPTS) {
                    delay(500)
                }
            }
        }

        return@withContext if (connected) {
            setStatus(BluetoothConnectionStatus.CONNECTED)
            BluetoothConnectionResult.Connected
        } else {
            BluetoothConnectionResult.ErrorConnecting(lastException)
        }
    }

    suspend fun disconnect() = withContext(ioDispatcher) {
        statusMutex.withLock {
            if (status != BluetoothConnectionStatus.CONNECTED) {
                return@withContext
            }
        }
        socket?.close()
        setStatus(BluetoothConnectionStatus.DISCONNECTED)
    }

    suspend fun write(bytes: ByteArray) = withContext(ioDispatcher) {
        outStream?.write(bytes)
        outStream?.flush()
    }

    suspend fun sendData(data: ByteArray) = withContext(ioDispatcher) {
        write(data)
    }

    suspend fun isConnected(): Boolean {
        return statusMutex.withLock {
            status == BluetoothConnectionStatus.CONNECTED
        }
    }

    companion object {
        private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private const val MAX_ATTEMPTS = 4
    }

    sealed interface BluetoothConnectionResult {
        data object BluetoothDisabled : BluetoothConnectionResult
        data object InvalidAddress : BluetoothConnectionResult
        data object Connecting : BluetoothConnectionResult
        data object Connected : BluetoothConnectionResult
        data class ErrorConnecting(val exception: IOException?) : BluetoothConnectionResult
    }

    enum class BluetoothConnectionStatus {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
    }
}