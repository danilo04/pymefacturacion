package com.walkyriasys.pyme.facturacion.domain.database

import InvoiceItem
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.walkyriasys.pyme.facturacion.domain.database.dao.InvoiceDao
import com.walkyriasys.pyme.facturacion.domain.database.dao.ProductDao
import com.walkyriasys.pyme.facturacion.domain.database.models.Invoice
import com.walkyriasys.pyme.facturacion.domain.database.models.Product

private const val DATABASE_VERSION = 1

@Database(
    entities = [Invoice::class, InvoiceItem::class, Product::class],
    version = DATABASE_VERSION,
)
@TypeConverters(ProductTypeConverter::class)
abstract class PymeFacturacionDatabase : RoomDatabase() {
    abstract fun invoiceDao(): InvoiceDao
    abstract fun productDao(): ProductDao
}