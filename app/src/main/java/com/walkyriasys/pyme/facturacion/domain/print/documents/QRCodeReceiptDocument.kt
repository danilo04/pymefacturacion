package com.walkyriasys.pyme.facturacion.domain.print.documents

import com.walkyriasys.pyme.facturacion.domain.print.drivers.PrinterDriver
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem
import com.walkyriasys.pyme.facturacion.domain.database.models.Product
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Document for printing laundry order receipts with QR codes
 */
data class QRCodeReceiptDocument(
    val order: Order,
    val orderItems: List<OrderItem> = emptyList(),
    val contactPhone: String? = null,
    val contactEmail: String? = null
) : Document {
    override val metadata: Map<String, String>
        get() = mapOf(
            "orderId" to order.id.toString(),
            "customerName" to order.customerName,
            "businessName" to "Lavanderia Walki",
            "deliveryDate" to (order.expectedDeliveryDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "N/A")
        )
    
    override suspend fun print(printer: PrinterDriver) {
        printHeader(printer)
        printBody(printer)
        printFooter(printer)
        
        // Add extra spacing at the end
        printer.printLineBreak()
        printer.printLineBreak()
        printer.printLineBreak()
    }
    
    override suspend fun printHeader(printer: PrinterDriver) {
        // Business name as H1 header
        printer.printH1("Lavanderia Walki")
        printer.printLineBreak()
        
        // Order details header
        printer.printH3("Order Details")
        printer.printLineBreak()
        
        // Customer information
        printer.printParagraph("Customer: ${order.customerName}")
        
        // Order number
        printer.printParagraph("Order #: ${order.id}")
        
        // Created date
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        printer.printParagraph("Date: ${order.createdAt.format(dateFormatter)}")
        
        // Delivery date
        val deliveryDateStr = order.expectedDeliveryDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "N/A"
        printer.printParagraph("Delivery Date: $deliveryDateStr")
        
        printer.printLineBreak()
        
        // Separator
        printer.printParagraph("================================")
        printer.printLineBreak()
    }
    
    override suspend fun printBody(printer: PrinterDriver) {
        // QR Code section
        printer.printH4("Order QR Code")
        printer.printParagraph("Scan to track your order:")
        printer.printLineBreak()
        
        // Create QR code data in the specified format:
        // ORDER_ID|CUSTOMER_NAME|DATE|DELIVERY_DATE|BUSINESS_NAME
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val deliveryDateStr = order.expectedDeliveryDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "N/A"
        
        val qrData = "${order.id}|${order.customerName}|${order.createdAt.format(dateFormatter)}|$deliveryDateStr|Lavanderia Walki"
        
        // Print the QR code
        printer.printQRCode(qrData, 200)
        
        printer.printLineBreak()
        

    }
    
    override suspend fun printFooter(printer: PrinterDriver) {
        printer.printLineBreak()
        printer.printParagraph("================================")
        
        // Contact information
        printer.printH5("Contact Information")
        printer.printParagraph("Phone: ${contactPhone ?: "N/A"}")
        printer.printParagraph("Email: ${contactEmail ?: "N/A"}")
        
        printer.printLineBreak()
        
        // Thank you message
        printer.printH4("Thank you for choosing")
        printer.printH4("Lavanderia Walki!")
        printer.printParagraph("Your clothes are in good hands.")
        
        printer.printLineBreak()
        printer.printParagraph("Keep this receipt for pickup.")
    }
    
    override fun validate(): Boolean {
        return order.id > 0 &&
                order.customerName.isNotBlank()
    }
    
    override fun getEstimatedLineCount(): Int {
        // Header: ~12 lines
        // Body: QR code (~8 lines) + items + status (~5 lines)
        // Footer: ~10 lines
        val itemLines = orderItems.size
        return 12 + 8 + 5 + itemLines + 10
    }
    
    override fun getRequiredCapabilities(): Set<PrinterCapability> {
        return setOf(PrinterCapability.QR_CODE, PrinterCapability.BASIC_TEXT)
    }
}