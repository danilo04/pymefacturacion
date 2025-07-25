package com.walkyriasys.pyme.facturacion.domain.print

import com.dayoneapp.dayone.di.IOThreadDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrinterService @Inject constructor(
    private val bluetoothConnectionManager: BluetoothConnectionManager,
    @IOThreadDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun printTestDocument(): Result<Unit> = withContext(ioDispatcher) {
        try {
            // Check if connected
            if (!bluetoothConnectionManager.isConnected()) {
                return@withContext Result.failure(Exception("Printer not connected"))
            }

            // Initialize printer
            val initCommand = PrinterCommand.POS_Set_PrtInit()
            bluetoothConnectionManager.sendData(initCommand)

            // Print header
            printHeader()
            
            // Print test content
            printTestContent()
            
            // Print footer with date/time
            printFooter()
            
            // Cut paper
            val cutCommand = byteArrayOf(0x0a, 0x1d, 0x56, 0x42, 0x01, 0x0a)
            bluetoothConnectionManager.sendData(cutCommand)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun printHeader() {
        // Center alignment
        val centerAlign = byteArrayOf(0x1b, 0x61, 0x01)
        bluetoothConnectionManager.sendData(centerAlign)
        
        // Double size
        val doubleSize = byteArrayOf(0x1d, 0x21, 0x11)
        bluetoothConnectionManager.sendData(doubleSize)
        
        // Store name
        val storeName = "PYME FACTURACIÓN\n"
        bluetoothConnectionManager.sendData(storeName.toByteArray(Charsets.UTF_8))
        
        // Reset size
        val normalSize = byteArrayOf(0x1d, 0x21, 0x00)
        bluetoothConnectionManager.sendData(normalSize)
        
        // Left alignment
        val leftAlign = byteArrayOf(0x1b, 0x61, 0x00)
        bluetoothConnectionManager.sendData(leftAlign)
        
        bluetoothConnectionManager.sendData("\n".toByteArray())
    }

    private suspend fun printTestContent() {
        val testContent = """
        ================================
        TEST DOCUMENT
        ================================
        
        This is a test print from the
        PYME Facturación app.
        
        Document Details:
        - Type: Test Receipt
        - Status: Sample
        - Items: Demo Data
        
        Products:
        Product A        ${'$'}10.00
        Product B        ${'$'}25.50
        Product C        ${'$'}15.75
        --------------------------------
        Subtotal:        ${'$'}51.25
        Tax:             ${'$'}5.13
        Total:           ${'$'}56.38
        
        Payment Method: Cash
        Change:          ${'$'}3.62
        
        ================================
        Thank you for your business!
        ================================
        
        """.trimIndent()
        
        bluetoothConnectionManager.sendData(testContent.toByteArray(Charsets.UTF_8))
    }

    private suspend fun printFooter() {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = formatter.format(Date())
        
        // Center alignment
        val centerAlign = byteArrayOf(0x1b, 0x61, 0x01)
        bluetoothConnectionManager.sendData(centerAlign)
        
        val footer = "Printed: $currentDate\n"
        bluetoothConnectionManager.sendData(footer.toByteArray(Charsets.UTF_8))
        
        // Left alignment
        val leftAlign = byteArrayOf(0x1b, 0x61, 0x00)
        bluetoothConnectionManager.sendData(leftAlign)
        
        bluetoothConnectionManager.sendData("\n\n\n".toByteArray())
    }

    suspend fun printQRCode(data: String): Result<Unit> = withContext(ioDispatcher) {
        try {
            if (!bluetoothConnectionManager.isConnected()) {
                return@withContext Result.failure(Exception("Printer not connected"))
            }

            // Generate QR code command using the SDK
            val qrCodeCommand = PrinterCommand.getBarCommand(data, 1, 3, 8)
            
            // Center alignment
            val centerAlign = byteArrayOf(0x1b, 0x61, 0x01)
            bluetoothConnectionManager.sendData(centerAlign)
            
            bluetoothConnectionManager.sendData("QR Code:\n".toByteArray())
            bluetoothConnectionManager.sendData(qrCodeCommand!!)
            
            // Left alignment
            val leftAlign = byteArrayOf(0x1b, 0x61, 0x00)
            bluetoothConnectionManager.sendData(leftAlign)
            
            bluetoothConnectionManager.sendData("\n".toByteArray())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun printBarcode(data: String, type: Int = 73): Result<Unit> = withContext(ioDispatcher) {
        try {
            if (!bluetoothConnectionManager.isConnected()) {
                return@withContext Result.failure(Exception("Printer not connected"))
            }

            // Generate barcode command (type 73 = CODE128)
            val barcodeCommand = PrinterCommand.getCodeBarCommand(data, type, 3, 168, 1, 2)
            
            bluetoothConnectionManager.sendData("Barcode:\n".toByteArray())
            
            // Center alignment
            val centerAlign = byteArrayOf(0x1b, 0x61, 0x01)
            bluetoothConnectionManager.sendData(centerAlign)
            
            bluetoothConnectionManager.sendData(barcodeCommand!!)
            
            // Left alignment  
            val leftAlign = byteArrayOf(0x1b, 0x61, 0x00)
            bluetoothConnectionManager.sendData(leftAlign)
            
            bluetoothConnectionManager.sendData("\n".toByteArray())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
