package com.walkyriasys.pyme.facturacion.domain.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.walkyriasys.pyme.facturacion.domain.database.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM ${Product.TABLE_NAME} ORDER BY ${Product.NAME} ASC")
    fun getAllProducts(): List<Product>

    @Query(
        "SELECT * FROM ${Product.TABLE_NAME} " +
                "ORDER BY ${Product.NAME} ASC " +
                "LIMIT :limit OFFSET :offset"
    )
    fun getProducts(limit: Int, offset: Int): Flow<List<Product>>
}