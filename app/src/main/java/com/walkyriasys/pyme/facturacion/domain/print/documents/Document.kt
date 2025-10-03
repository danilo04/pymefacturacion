package com.walkyriasys.pyme.facturacion.domain.print.documents

import com.walkyriasys.pyme.facturacion.domain.print.drivers.PrinterDriver


/**
 * Interface representing a printable document
 */
interface Document {
    
    /**
     * Additional metadata for the document
     */
    val metadata: Map<String, String>
        get() = emptyMap()
    
    /**
     * Prints the document using the provided printer driver
     * This method should define the complete printing sequence
     * @param printer The printer driver to use for printing
     */
    suspend fun print(printer: PrinterDriver)
    
    /**
     * Prints the document header
     * @param printer The printer driver to use for printing
     */
    suspend fun printHeader(printer: PrinterDriver)
    
    /**
     * Prints the document body/content
     * @param printer The printer driver to use for printing
     */
    suspend fun printBody(printer: PrinterDriver)
    
    /**
     * Prints the document footer
     * @param printer The printer driver to use for printing
     */
    suspend fun printFooter(printer: PrinterDriver)

    /**
     * Validates the document data before printing
     * @return true if the document is valid and ready to print, false otherwise
     */
    fun validate(): Boolean

    /**
     * Gets the estimated number of lines this document will print
     * Useful for paper management and print preview
     * @return Estimated number of lines
     */
    fun getEstimatedLineCount(): Int
    
    /**
     * Checks if the document requires specific printer capabilities
     * @return Set of required capabilities (e.g., "QR_CODE", "BARCODE", "GRAPHICS")
     */
    fun getRequiredCapabilities(): Set<PrinterCapability>
}

enum class PrinterCapability {
    QR_CODE,
    BARCODE,
    GRAPHICS,
    TABLE,
    BASIC_TEXT
}