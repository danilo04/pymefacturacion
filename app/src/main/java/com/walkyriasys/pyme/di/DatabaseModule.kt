package com.walkyriasys.pyme.di

import android.content.Context
import com.walkyriasys.pyme.facturacion.domain.database.PymeFacturacionDatabase
import com.walkyriasys.pyme.facturacion.domain.database.dao.InvoiceDao
import com.walkyriasys.pyme.facturacion.domain.database.dao.ProductDao
import com.walkyriasys.pyme.facturacion.domain.database.dao.OrderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): PymeFacturacionDatabase {
        return PymeFacturacionDatabase.buildDatabase(appContext)
    }

    @Provides
    fun providesProductDao(pymeFacturacionDatabase: PymeFacturacionDatabase): ProductDao {
        return pymeFacturacionDatabase.productDao()
    }

    @Provides
    fun providesInvoiceDao(pymeFacturacionDatabase: PymeFacturacionDatabase): InvoiceDao {
        return pymeFacturacionDatabase.invoiceDao()
    }

    @Provides
    fun providesOrderDao(pymeFacturacionDatabase: PymeFacturacionDatabase): OrderDao {
        return pymeFacturacionDatabase.orderDao()
    }
}