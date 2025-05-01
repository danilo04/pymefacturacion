package com.walkyriasys.pyme.facturacion.ui.models

import java.math.BigDecimal

// Helper data class for UI state
data class OrderItemEntry(
    val productId: Int,
    val productName: String,
    val price: BigDecimal,
    val quantity: Int
)