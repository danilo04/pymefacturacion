package com.walkyriasys.pyme.facturacion.domain.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(
    tableName = Invoice.TABLE_NAME,
    indices = [
        Index(value = [Invoice.UUID], unique = true),
        Index(value = [Invoice.DATE]),
        Index(value = [Invoice.CUSTOMER_NAME]),
    ]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID) 
    val id: Int,
    @ColumnInfo(name = UUID)
    val uuid: String,
    @ColumnInfo(name = DATE)
    val date: String,
    @ColumnInfo(name = CUSTOMER_NAME)
    val customerName: String,
    @ColumnInfo(name = TOTAL)
    val total: Int, // cents of currency,
    @ColumnInfo(name = SUB_TOTAL)
    val subtotal: Int, // cents of currency
    @ColumnInfo(name = TAX)
    val tax: Int, // cents of currency
) {
    companion object {
        const val TABLE_NAME = "invoices"
        const val CUSTOMER_NAME = "customer_name"
        const val TOTAL = "total"
        const val SUB_TOTAL = "subtotal"
        const val TAX = "tax"
        const val UUID = "uuid"
        const val DATE = "date"
        const val ID = "id"
    }   
}