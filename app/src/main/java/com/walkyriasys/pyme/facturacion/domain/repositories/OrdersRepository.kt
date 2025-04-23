package com.walkyriasys.pyme.facturacion.domain.repositories

import com.dayoneapp.dayone.di.IOThreadDispatcher
import com.walkyriasys.pyme.facturacion.domain.database.dao.OrderDao
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrdersRepository @Inject constructor(
    @IOThreadDispatcher private val ioThreadDispatcher: CoroutineDispatcher,
    private val orderDao: OrderDao
) {
    fun getOrders(page: Int = 0): Flow<List<Order>> {
        return orderDao.getOrders(PAGE_SIZE, page * PAGE_SIZE).flowOn(ioThreadDispatcher)
    }

    suspend fun addOrder(order: Order): Long = withContext(ioThreadDispatcher) {
        return@withContext orderDao.insertOrder(order)
    }

    suspend fun addOrderItem(orderItem: OrderItem): Int = withContext(ioThreadDispatcher) {
        return@withContext orderDao.insertOrderItem(orderItem).toInt()
    }

    suspend fun getOrderWithItems(orderId: Long): Order? = withContext(ioThreadDispatcher) {
        return@withContext orderDao.getOrderById(orderId)
    }

    suspend fun getOrderItems(orderId: Long): List<OrderItem> = withContext(ioThreadDispatcher) {
        return@withContext orderDao.getOrderItems(orderId)
    }

    suspend fun updateOrder(order: Order) = withContext(ioThreadDispatcher) {
        orderDao.updateOrder(order)
    }

    suspend fun deleteOrder(order: Order) = withContext(ioThreadDispatcher) {
        orderDao.deleteOrder(order)
    }

    companion object {
        private const val PAGE_SIZE = 50
    }
}