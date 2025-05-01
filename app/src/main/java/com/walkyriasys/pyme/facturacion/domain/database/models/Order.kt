package com.walkyriasys.pyme.facturacion.domain.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = Order.TABLE_NAME)
data class Order(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long = 0,
    @ColumnInfo(name = STATUS)
    val orderStatus: OrderStatus = OrderStatus.PENDING,
    @ColumnInfo(name = EXPECTED_DELIVERY_DATE)
    val expectedDeliveryDate: LocalDateTime? = null,
    @ColumnInfo(name = TOTAL_AMOUNT)
    val totalAmount: Int,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: LocalDate = LocalDate.now(),
    @ColumnInfo(name = CUSTOMER_NAME)
    val customerName: String,
) {
    companion object {
        const val ID = "id"
        const val STATUS = "status"
        const val EXPECTED_DELIVERY_DATE = "expected_delivery_date"
        const val TOTAL_AMOUNT = "total_amount"
        const val CREATED_AT = "created_at"
        const val CUSTOMER_NAME = "customer_name"
        const val TABLE_NAME = "orders"
    }

    enum class OrderStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        DELIVERED
    }
}