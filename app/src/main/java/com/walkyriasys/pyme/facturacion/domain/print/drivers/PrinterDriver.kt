package com.walkyriasys.pyme.facturacion.domain.print.drivers

interface PrinterDriver {
    
    /**
     * Establishes connection with the printer
     * @return true if connection successful, false otherwise
     */
    suspend fun connect(): Boolean
    
    /**
     * Disconnects from the printer
     * @return true if disconnection successful, false otherwise
     */
    suspend fun disconnect(): Boolean
    
    /**
     * Checks if the printer is currently connected
     * @return true if connected, false otherwise
     */
    fun isConnected(): Boolean
    
    /**
     * Prints text with H1 formatting (largest header)
     * @param text The text to print
     */
    suspend fun printH1(text: String)
    
    /**
     * Prints text with H2 formatting (large header)
     * @param text The text to print
     */
    suspend fun printH2(text: String)
    
    /**
     * Prints text with H3 formatting (medium header)
     * @param text The text to print
     */
    suspend fun printH3(text: String)
    
    /**
     * Prints text with H4 formatting (small header)
     * @param text The text to print
     */
    suspend fun printH4(text: String)
    
    /**
     * Prints text with H5 formatting (smallest header)
     * @param text The text to print
     */
    suspend fun printH5(text: String)
    
    /**
     * Prints a paragraph of text with normal formatting
     * @param text The paragraph text to print
     */
    suspend fun printParagraph(text: String)
    
    /**
     * Prints a QR code
     * @param data The data to encode in the QR code
     * @param size The size of the QR code (optional, default implementation specific)
     */
    suspend fun printQRCode(data: String, size: Int = 200)
    
    /**
     * Prints a barcode
     * @param data The data to encode in the barcode
     * @param type The barcode type (e.g., CODE128, EAN13, etc.)
     * @param width The width of the barcode (optional)
     * @param height The height of the barcode (optional)
     */
    suspend fun printBarcode(
        data: String, 
        type: BarcodeType = BarcodeType.CODE128,
        width: Int = 300,
        height: Int = 100
    )
    
    /**
     * Prints a line break
     */
    suspend fun printLineBreak()
    
    /**
     * Prints a table with headers and rows
     * @param headers List of column headers
     * @param rows List of rows, where each row is a list of cell values
     * @param columnWidths Optional list of column widths (characters per column)
     */
    suspend fun printTable(
        headers: List<String>,
        rows: List<List<String>>,
        columnWidths: List<Int>? = null
    )
}

/**
 * Enum representing different barcode types
 */
enum class BarcodeType {
    CODE128,
    CODE39,
    EAN13,
    EAN8,
    UPC_A,
    UPC_E,
    ITF,
    CODABAR
}