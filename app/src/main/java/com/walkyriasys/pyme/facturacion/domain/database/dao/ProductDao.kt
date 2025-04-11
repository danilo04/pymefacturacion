package com.walkyriasys.pyme.facturacion.domain.database.dao

import androidx.room.*
import com.walkyriasys.pyme.facturacion.domain.database.models.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM ${Product.TABLE_NAME} ORDER BY ${Product.NAME} ASC")
    fun getAllProducts(): List<Product>
}