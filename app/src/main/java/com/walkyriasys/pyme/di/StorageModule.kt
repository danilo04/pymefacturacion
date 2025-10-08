package com.walkyriasys.pyme.di

import android.content.Context
import com.walkyriasys.pyme.storage.StorageConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun provideStorageConfiguration(@ApplicationContext appContext: Context): StorageConfiguration {
        val baseDir: String = appContext.filesDir.absolutePath + File.separator
        val productsDir = baseDir + File.separator + "products" + File.separator + "photos"

        return StorageConfiguration(
            productsDir = productsDir,
            contentResolver = appContext.contentResolver
        )
    }
}