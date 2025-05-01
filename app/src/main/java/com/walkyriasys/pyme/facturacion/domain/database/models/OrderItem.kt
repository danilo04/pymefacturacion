package com.walkyriasys.pyme.facturacion.domain.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = OrderItem.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = [Order.ID],
            childColumns = [OrderItem.ORDER_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = [Product.ID],
            childColumns = [OrderItem.PRODUCT_ID],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = [OrderItem.ORDER_ID]),
        Index(value = [OrderItem.PRODUCT_ID]),
        Index(value = [OrderItem.UUID], unique = true), // Unique index for uuid
    ]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int,
    @ColumnInfo(name = UUID)
    val uuid: String,
    @ColumnInfo(name = ORDER_ID)
    val orderId: Long,
    @ColumnInfo(name = PRODUCT_ID)
    val productId: Int,
    @ColumnInfo(name = QUANTITY)
    val quantity: Int,
    @ColumnInfo(name = PRICE)
    val price: Long, // cents of currency
    @ColumnInfo(name = DISCOUNT)
    val discount: Int? // cents of currency
) {
    companion object {
        const val TABLE_NAME = "order_items"
        const val UUID = "uuid"
        const val ORDER_ID = "order_id"
        const val PRODUCT_ID = "product_id"
        const val QUANTITY = "quantity"
        const val PRICE = "price"
        const val DISCOUNT = "discount"
        const val ID = "id"
    }
}