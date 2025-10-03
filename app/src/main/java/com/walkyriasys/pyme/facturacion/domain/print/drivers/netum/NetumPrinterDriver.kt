package com.walkyriasys.pyme.facturacion.domain.print.drivers.netum

import com.walkyriasys.pyme.facturacion.domain.print.drivers.BarcodeType
import com.walkyriasys.pyme.facturacion.domain.print.BluetoothConnectionManager
import com.walkyriasys.pyme.facturacion.domain.print.drivers.PrinterDriver
import com.walkyriasys.pyme.facturacion.domain.preferences.BluetoothPreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetumPrinterDriver @Inject constructor(
    private val bluetoothConnectionManager: BluetoothConnectionManager,
    private val bluetoothPreferencesManager: BluetoothPreferencesManager
) : PrinterDriver {

    override suspend fun connect(): Boolean {
        val savedAddress = bluetoothPreferencesManager.getSelectedBluetoothDeviceAddress()
        return if (savedAddress != null) {
            when (bluetoothConnectionManager.connect(savedAddress)) {
                is BluetoothConnectionManager.BluetoothConnectionResult.Connected -> {
                    // Initialize printer after successful connection
                    initializePrinter()
                    true
                }
                else -> false
            }
        } else {
            false
        }
    }

    override suspend fun disconnect(): Boolean {
        return try {
            bluetoothConnectionManager.disconnect()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun isConnected(): Boolean {
        return try {
            // Since isConnected is suspend function in BluetoothConnectionManager,
            // we need to handle this differently or make it synchronous
            // For now, we'll check the connection status based on available methods
            bluetoothConnectionManager.hashCode() != 0 // Placeholder - need to implement proper status check
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun initializePrinter() {
        try {
            val initCommand = PrinterCommand.POS_Set_PrtInit()
            bluetoothConnectionManager.sendData(initCommand)
        } catch (e: Exception) {
            // Handle initialization error
        }
    }

    override suspend fun printH1(text: String) {
        try {
            // Set large font size for H1 (4x width, 4x height)
            val fontSizeCommand = PrinterCommand.POS_Set_FontSize(3, 3)
            bluetoothConnectionManager.sendData(fontSizeCommand!!)
            
            // Set bold
            val boldCommand = PrinterCommand.POS_Set_Bold(1)
            bluetoothConnectionManager.sendData(boldCommand)
            
            // Center align
            val alignCommand = PrinterCommand.POS_S_Align(1)
            alignCommand?.let {
                bluetoothConnectionManager.sendData(it)
            }

            // Print text
            val textCommand = PrinterCommand.POS_Print_Text(0, text, "GBK")
            bluetoothConnectionManager.sendData(textCommand!!)
            
            // Line break
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)
            
            // Reset formatting
            resetFormatting()
        } catch (e: Exception) {
            throw RuntimeException("Failed to print H1: ${e.message}", e)
        }
    }

    override suspend fun printH2(text: String) {
        try {
            // Set large font size for H2 (3x width, 3x height)
            val fontSizeCommand = PrinterCommand.POS_Set_FontSize(2, 2)
            bluetoothConnectionManager.sendData(fontSizeCommand!!)
            
            // Set bold
            val boldCommand = PrinterCommand.POS_Set_Bold(1)
            bluetoothConnectionManager.sendData(boldCommand)
            
            // Print text
            val textCommand = PrinterCommand.POS_Print_Text(0, text, "GBK")
            bluetoothConnectionManager.sendData(textCommand!!)
            
            // Line break
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)
            
            // Reset formatting
            resetFormatting()
        } catch (e: Exception) {
            throw RuntimeException("Failed to print H2: ${e.message}", e)
        }
    }

    override suspend fun printH3(text: String) {
        try {
            // Set medium font size for H3 (2x width, 2x height)
            val fontSizeCommand = PrinterCommand.POS_Set_FontSize(1, 1)
            bluetoothConnectionManager.sendData(fontSizeCommand!!)
            
            // Set bold
            val boldCommand = PrinterCommand.POS_Set_Bold(1)
            bluetoothConnectionManager.sendData(boldCommand)
            
            // Print text
            val textCommand = PrinterCommand.POS_Print_Text(0, text, "GBK")
            bluetoothConnectionManager.sendData(textCommand!!)
            
            // Line break
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)
            
            // Reset formatting
            resetFormatting()
        } catch (e: Exception) {
            throw RuntimeException("Failed to print H3: ${e.message}", e)
        }
    }

    override suspend fun printH4(text: String) {
        try {
            // Set small font size for H4 (1x width, 1x height)
            val fontSizeCommand = PrinterCommand.POS_Set_FontSize(0, 0)
            bluetoothConnectionManager.sendData(fontSizeCommand!!)
            
            // Set bold
            val boldCommand = PrinterCommand.POS_Set_Bold(1)
            bluetoothConnectionManager.sendData(boldCommand)
            
            // Print text
            val textCommand = PrinterCommand.POS_Print_Text(0, text, "GBK")
            bluetoothConnectionManager.sendData(textCommand!!)
            
            // Line break
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)
            
            // Reset formatting
            resetFormatting()
        } catch (e: Exception) {
            throw RuntimeException("Failed to print H4: ${e.message}", e)
        }
    }

    override suspend fun printH5(text: String) {
        try {
            // Set smallest font size for H5 (normal size but bold)
            val fontSizeCommand = PrinterCommand.POS_Set_FontSize(0, 0)
            bluetoothConnectionManager.sendData(fontSizeCommand!!)
            
            // Set bold
            val boldCommand = PrinterCommand.POS_Set_Bold(1)
            bluetoothConnectionManager.sendData(boldCommand)
            
            // Print text
            val textCommand = PrinterCommand.POS_Print_Text(0, text, "GBK")
            bluetoothConnectionManager.sendData(textCommand!!)
            
            // Line break
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)
            
            // Reset formatting
            resetFormatting()
        } catch (e: Exception) {
            throw RuntimeException("Failed to print H5: ${e.message}", e)
        }
    }

    override suspend fun printParagraph(text: String) {
        try {
            // Set normal font size
            val fontSizeCommand = PrinterCommand.POS_Set_FontSize(0, 0)
            bluetoothConnectionManager.sendData(fontSizeCommand!!)
            
            // Left align
            // Center align
            val alignCommand = PrinterCommand.POS_S_Align(0)
            alignCommand?.let {
                bluetoothConnectionManager.sendData(it)
            }

            // Print text
            val textCommand = PrinterCommand.POS_Print_Text(0, text, "GBK")
            bluetoothConnectionManager.sendData(textCommand!!)
            
            // Line break
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)
        } catch (e: Exception) {
            throw RuntimeException("Failed to print paragraph: ${e.message}", e)
        }
    }

    override suspend fun printQRCode(data: String, size: Int) {
        try {
            // Generate QR code command
            // Parameters: data, version (1-19), error correction level (0-3), magnification (1-8)
            val magnification = when {
                size <= 100 -> 3
                size <= 150 -> 5
                size <= 200 -> 6
                else -> 8
            }
            
            val qrCodeCommand = PrinterCommand.getBarCommand(data, 1, 3, magnification)
            bluetoothConnectionManager.sendData(qrCodeCommand!!)
            
            // Line break
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)

            // Reset alignment
            val leftAlign = byteArrayOf(0x1b, 0x61, 0x00)
            bluetoothConnectionManager.sendData(leftAlign)
        } catch (e: Exception) {
            throw RuntimeException("Failed to print QR code: ${e.message}", e)
        }
    }

    override suspend fun printBarcode(data: String, type: BarcodeType, width: Int, height: Int) {
        try {
            // Center align for barcode
            val alignCommand = PrinterCommand.POS_S_Align(1)
            bluetoothConnectionManager.sendData(alignCommand!!)
            
            // Map BarcodeType to printer command type
            val printerBarcodeType = when (type) {
                BarcodeType.CODE128 -> 73
                BarcodeType.CODE39 -> 69
                BarcodeType.EAN13 -> 67
                BarcodeType.EAN8 -> 68
                BarcodeType.UPC_A -> 65
                BarcodeType.UPC_E -> 66
                BarcodeType.ITF -> 70
                BarcodeType.CODABAR -> 71
            }
            
            // Calculate width and height parameters for printer
            val printerWidth = minOf(6, maxOf(2, width / 50)) // Scale to 2-6 range
            val printerHeight = minOf(255, maxOf(50, height)) // Scale to 50-255 range
            
            // Generate barcode command
            val barcodeCommand = PrinterCommand.getCodeBarCommand(
                data, 
                printerBarcodeType, 
                printerWidth, 
                printerHeight, 
                1, // HRI font type
                2  // HRI position (below barcode)
            )
            bluetoothConnectionManager.sendData(barcodeCommand!!)
            
            // Line break
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)
            
            // Reset alignment
            val leftAlignCommand = PrinterCommand.POS_S_Align(0)
            bluetoothConnectionManager.sendData(leftAlignCommand!!)
        } catch (e: Exception) {
            throw RuntimeException("Failed to print barcode: ${e.message}", e)
        }
    }

    override suspend fun printLineBreak() {
        try {
            val lineBreakCommand = PrinterCommand.POS_Set_LF()
            bluetoothConnectionManager.sendData(lineBreakCommand)
        } catch (e: Exception) {
            throw RuntimeException("Failed to print line break: ${e.message}", e)
        }
    }

    override suspend fun printTable(
        headers: List<String>,
        rows: List<List<String>>,
        columnWidths: List<Int>?
    ) {
        try {
            // Calculate default column widths if not provided
            val calculatedWidths = columnWidths ?: calculateColumnWidths(headers, rows)
            
            // Ensure we have the right number of column widths
            val finalWidths = if (calculatedWidths.size < headers.size) {
                calculatedWidths + List(headers.size - calculatedWidths.size) { 10 }
            } else {
                calculatedWidths.take(headers.size)
            }
            
            // Print table header
            printTableHeader(headers, finalWidths)
            
            // Print separator line
            printTableSeparator(finalWidths)
            
            // Print table rows
            rows.forEach { row ->
                printTableRow(row, finalWidths)
            }
            
            // Print bottom separator
            printTableSeparator(finalWidths)
            
            // Add a line break after the table
            printLineBreak()
        } catch (e: Exception) {
            throw RuntimeException("Failed to print table: ${e.message}", e)
        }
    }
    
    private fun calculateColumnWidths(headers: List<String>, rows: List<List<String>>): List<Int> {
        val minWidth = 8
        val maxWidth = 20
        
        return headers.mapIndexed { index, header ->
            // Start with header length
            var maxLength = header.length
            
            // Check all rows for this column
            rows.forEach { row ->
                if (index < row.size) {
                    maxLength = maxOf(maxLength, row[index].length)
                }
            }
            
            // Apply min/max constraints
            maxOf(minWidth, minOf(maxWidth, maxLength))
        }
    }
    
    private suspend fun printTableHeader(headers: List<String>, columnWidths: List<Int>) {
        // Set bold for headers
        val boldCommand = PrinterCommand.POS_Set_Bold(1)
        bluetoothConnectionManager.sendData(boldCommand)
        
        // Build header row
        val headerRow = buildTableRow(headers, columnWidths)
        
        // Print header
        val textCommand = PrinterCommand.POS_Print_Text(0, headerRow, "GBK")
        bluetoothConnectionManager.sendData(textCommand!!)
        
        // Line break
        val lineBreakCommand = PrinterCommand.POS_Set_LF()
        bluetoothConnectionManager.sendData(lineBreakCommand)
        
        // Remove bold
        val noBoldCommand = PrinterCommand.POS_Set_Bold(0)
        bluetoothConnectionManager.sendData(noBoldCommand)
    }
    
    private suspend fun printTableRow(row: List<String>, columnWidths: List<Int>) {
        val tableRow = buildTableRow(row, columnWidths)
        
        // Print row
        val textCommand = PrinterCommand.POS_Print_Text(0, tableRow, "GBK")
        bluetoothConnectionManager.sendData(textCommand!!)
        
        // Line break
        val lineBreakCommand = PrinterCommand.POS_Set_LF()
        bluetoothConnectionManager.sendData(lineBreakCommand)
    }
    
    private suspend fun printTableSeparator(columnWidths: List<Int>) {
        val separator = columnWidths.joinToString("+") { "-".repeat(it) }
        val fullSeparator = "+$separator+"
        
        // Print separator
        val textCommand = PrinterCommand.POS_Print_Text(0, fullSeparator, "GBK")
        bluetoothConnectionManager.sendData(textCommand!!)
        
        // Line break
        val lineBreakCommand = PrinterCommand.POS_Set_LF()
        bluetoothConnectionManager.sendData(lineBreakCommand)
    }
    
    private fun buildTableRow(cells: List<String>, columnWidths: List<Int>): String {
        val paddedCells = cells.mapIndexed { index, cell ->
            val width = if (index < columnWidths.size) columnWidths[index] else 10
            cell.take(width).padEnd(width)
        }
        return "|${paddedCells.joinToString("|")}|"
    }

    private suspend fun resetFormatting() {
        try {
            // Reset font size to normal
            val fontSizeCommand = PrinterCommand.POS_Set_FontSize(0, 0)
            bluetoothConnectionManager.sendData(fontSizeCommand!!)
            
            // Remove bold
            val boldCommand = PrinterCommand.POS_Set_Bold(0)
            bluetoothConnectionManager.sendData(boldCommand)
            
            // Set left alignment
            val alignCommand = PrinterCommand.POS_S_Align(0)
            bluetoothConnectionManager.sendData(alignCommand!!)
        } catch (e: Exception) {
            // Handle reset error silently
        }
    }
}