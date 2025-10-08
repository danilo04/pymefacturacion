package com.walkyriasys.pyme.storage

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.Uuid

@Singleton
class StorageManager @Inject constructor(
    private val storageConfiguration: StorageConfiguration
) {
    suspend fun storeProductPhoto(productUuid: String, imageUri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            // Create products directory if it doesn't exist
            val productsDir = File(storageConfiguration.productsDir)
            if (!productsDir.exists()) {
                productsDir.mkdirs()
            }
            
            // Generate unique filename for the image
            val filename = "product_${productUuid}_${UUID.randomUUID()}.jpg"
            val destinationFile = File(productsDir, filename)
            
            // Copy image from URI to destination file
            storageConfiguration.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            
            // Return the filename
            destinationFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }
    }
}