package com.walkyriasys.pyme.facturacion.domain.database

import InvoiceItem
import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var instance: PymeFacturacionDatabase? = null

        fun buildDatabase(context: Context): PymeFacturacionDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    PymeFacturacionDatabase::class.java,
                    "pyme_facturacion_database"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}