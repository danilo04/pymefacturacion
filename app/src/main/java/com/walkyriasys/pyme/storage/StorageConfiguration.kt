package com.walkyriasys.pyme.storage

import android.content.ContentResolver

data class StorageConfiguration(
    val productsDir: String,
    val contentResolver: ContentResolver
)
