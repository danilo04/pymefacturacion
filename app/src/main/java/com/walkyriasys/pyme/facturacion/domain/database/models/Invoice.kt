package com.walkyriasys.pyme.facturacion.domain.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index

@Entity(
    tableName = Invoice.TABLE_NAME,
    indices = [
        Index(value = [Invoice.UUID], unique = true),
        Index(value = [Invoice.DATE])
    ]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID) 
    val id: Int,
    @ColumnInfo(name = UUID)
    val uuid: String,
    @ColumnInfo(name = DATE)
    val date: String
) {
    companion object {
        const val TABLE_NAME = "invoices"
        const val UUID = "uuid"
        const val DATE = "date"
        const val ID = "id"
    }   
}