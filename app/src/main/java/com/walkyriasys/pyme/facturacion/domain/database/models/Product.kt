package com.walkyriasys.pyme.facturacion.domain.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = Product.TABLE_NAME)
data class Product(
    @PrimaryKey(autoGenerate = true) 
    @ColumnInfo(name = ID)
    val id: Int = 0,
    @ColumnInfo(name = UUID)
    val uuid: String,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = DESCRIPTION)
    val description: String?,
    @ColumnInfo(name = PRICE)
    val price: Int, // cents of currency
    @ColumnInfo(name = STOCK_QUANTITY)
    val stockQuantity: Int?
) {
    companion object {
        const val TABLE_NAME = "products"
        const val ID = "id"
        const val UUID = "uuid"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val PRICE = "price"
        const val STOCK_QUANTITY = "stock_quantity"
    }
}