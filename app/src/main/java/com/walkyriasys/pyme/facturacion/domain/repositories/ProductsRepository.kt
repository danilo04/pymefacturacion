package com.walkyriasys.pyme.facturacion.domain.repositories

import com.dayoneapp.dayone.di.IOThreadDispatcher
import com.walkyriasys.pyme.facturacion.domain.database.dao.ProductDao
import com.walkyriasys.pyme.facturacion.domain.database.models.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(
    @IOThreadDispatcher private val ioThreadDispatcher: CoroutineDispatcher,
    private val productDao: ProductDao
) {
    fun getProducts(page: Int = 0): Flow<List<Product>> {
        return productDao.getProducts(PAGE_SIZE, page * PAGE_SIZE).flowOn(ioThreadDispatcher)
    }

    suspend fun addProduct(product: Product): Int = withContext(ioThreadDispatcher) {
        return@withContext productDao.insertProduct(product).toInt()
    }

    companion object {
        private const val PAGE_SIZE = 50
    }
}