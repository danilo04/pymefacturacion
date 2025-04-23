package com.walkyriasys.pyme.facturacion.domain.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(orderItem: OrderItem): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Update
    suspend fun updateOrderItem(orderItem: OrderItem)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Delete
    suspend fun deleteOrderItem(orderItem: OrderItem)

    @Query("SELECT * FROM ${Order.TABLE_NAME} ORDER BY ${Order.CREATED_AT} DESC")
    fun getAllOrders(): List<Order>

    @Query(
        "SELECT * FROM ${Order.TABLE_NAME} " +
                "ORDER BY ${Order.CREATED_AT} DESC " +
                "LIMIT :limit OFFSET :offset"
    )
    fun getOrders(limit: Int, offset: Int): Flow<List<Order>>

    @Query("SELECT * FROM ${Order.TABLE_NAME} WHERE ${Order.ID} = :orderId")
    suspend fun getOrderById(orderId: Long): Order?

    @Query("SELECT * FROM ${OrderItem.TABLE_NAME} WHERE ${OrderItem.ORDER_ID} = :orderId")
    suspend fun getOrderItems(orderId: Long): List<OrderItem>

    @Transaction
    @Query("SELECT * FROM ${Order.TABLE_NAME} WHERE ${Order.STATUS} = :status")
    fun getOrdersByStatus(status: Order.Status): Flow<List<Order>>
}