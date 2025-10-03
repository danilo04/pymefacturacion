package com.walkyriasys.pyme.facturacion.domain.print

import com.walkyriasys.pyme.facturacion.domain.print.documents.Document
import com.walkyriasys.pyme.facturacion.domain.print.documents.PrinterCapability
import com.walkyriasys.pyme.facturacion.domain.print.drivers.PrinterDriver
import com.walkyriasys.pyme.facturacion.domain.print.drivers.netum.NetumPrinterDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * DocumentPrinter handles the complete printing process for documents.
 * It manages printer connections and ensures proper cleanup after printing.
 */
class DocumentPrinter @Inject constructor(
    private val printer: PrinterDriver,
    private val disconnectAfterPrint: Boolean = true
) {
    
    /**
     * Prints the document with full connection management
     * @return PrintResult indicating success or failure with details
     */
    suspend fun print(document: Document): PrintResult = withContext(Dispatchers.IO) {
        return@withContext try {
            // Validate document before attempting to print
            if (!document.validate()) {
                return@withContext PrintResult.ValidationFailed("Document validation failed")
            }
            
            // Check if printer is already connected
            val wasAlreadyConnected = printer.isConnected()
            
            // Connect to printer if not already connected
            if (!wasAlreadyConnected) {
                val connected = printer.connect()
                if (!connected) {
                    return@withContext PrintResult.ConnectionFailed("Failed to connect to printer")
                }
            }
            
            try {
                // Print the document
                document.print(printer)
                
                PrintResult.Success
            } catch (printException: Exception) {
                PrintResult.PrintFailed("Printing failed: ${printException.message}", printException)
            } finally {
                // Disconnect if requested and we made the connection
                if (disconnectAfterPrint && !wasAlreadyConnected) {
                    try {
                        printer.disconnect()
                    } catch (disconnectException: Exception) {
                        // Log disconnect error but don't fail the overall operation
                        // since the document was already printed
                    }
                }
            }
            
        } catch (exception: Exception) {
            PrintResult.UnexpectedError("Unexpected error during printing: ${exception.message}", exception)
        }
    }
    
//    /**
//     * Checks if the printer supports all required capabilities for this document
//     */
//    suspend fun checkPrinterCompatibility(): CompatibilityResult {
//        val requiredCapabilities = document.getRequiredCapabilities()
//
//        // For now, we'll assume basic compatibility
//        // In a real implementation, you might check printer-specific capabilities
//        val supportedCapabilities = PrinterCapability.entries.toTypedArray()
//
//        val unsupportedCapabilities = requiredCapabilities - supportedCapabilities
//
//        return if (unsupportedCapabilities.isEmpty()) {
//            CompatibilityResult.Compatible
//        } else {
//            CompatibilityResult.Incompatible(unsupportedCapabilities)
//        }
//    }
    
    /**
     * Gets printer status information
     */
    fun getPrinterStatus(): PrinterStatus {
        return PrinterStatus(
            isConnected = printer.isConnected(),
            printerType = printer.javaClass.simpleName
        )
    }

    companion object {
        /**
         * Prints a specific document using this printer instance
         * Useful for printing multiple documents with the same printer
         */
        suspend fun print(printer: PrinterDriver, documentToPrint: Document, disconnectAfter: Boolean = true): PrintResult {
            return DocumentPrinter(printer, disconnectAfter).print(documentToPrint)
        }
    }
}

/**
 * Result of a print operation
 */
sealed class PrintResult {
    data object Success : PrintResult()
    data class ValidationFailed(val reason: String) : PrintResult()
    data class ConnectionFailed(val reason: String) : PrintResult()
    data class PrintFailed(val reason: String, val exception: Exception) : PrintResult()
    data class UnexpectedError(val reason: String, val exception: Exception) : PrintResult()
    
    val isSuccess: Boolean
        get() = this is Success
    
    val isFailure: Boolean
        get() = !isSuccess
}

/**
 * Result of printer compatibility check
 */
sealed class CompatibilityResult {
    object Compatible : CompatibilityResult()
    data class Incompatible(val unsupportedCapabilities: Set<PrinterCapability>) : CompatibilityResult()
    
    val isCompatible: Boolean
        get() = this is Compatible
}

/**
 * Printer status information
 */
data class PrinterStatus(
    val isConnected: Boolean,
    val printerType: String
)