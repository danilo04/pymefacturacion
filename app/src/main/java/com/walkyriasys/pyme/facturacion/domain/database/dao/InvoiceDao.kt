package com.walkyriasys.pyme.facturacion.domain.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.walkyriasys.pyme.facturacion.domain.database.models.Invoice

@Dao
interface InvoiceDao {
    @Insert
    suspend fun insert(invoice: Invoice)

    @Update
    suspend fun update(invoice: Invoice)

    @Delete
    suspend fun delete(invoice: Invoice)

    @Query("SELECT * FROM ${Invoice.TABLE_NAME} WHERE ${Invoice.ID} = :invoiceId")
    suspend fun getInvoiceById(invoiceId: Int): Invoice?
}