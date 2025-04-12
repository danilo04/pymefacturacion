package com.walkyriasys.pyme.facturacion.domain.database

import androidx.room.TypeConverter
import com.walkyriasys.pyme.facturacion.domain.database.models.ProductType

class ProductTypeConverter {
    @TypeConverter
    fun fromProductType(productType: ProductType): String {
        return productType.name
    }

    @TypeConverter
    fun toProductType(value: String): ProductType {
        return ProductType.valueOf(value)
    }
}