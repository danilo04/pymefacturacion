package com.walkyriasys.pyme.facturacion.domain.database

import androidx.room.TypeConverter
import com.walkyriasys.pyme.facturacion.domain.database.models.Order.OrderStatus
import com.walkyriasys.pyme.facturacion.domain.database.models.ProductType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

class OrderStatusConverter {
    @TypeConverter
    fun fromOrderStatus(orderStatus: OrderStatus): String {
        return orderStatus.name
    }

    @TypeConverter
    fun toOrderStatus(value: String): OrderStatus {
        return OrderStatus.valueOf(value)
    }
}

class LocalDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        return localDateTime?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it, formatter) }
    }
}

class LocalDateConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? {
        return localDate?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, formatter) }
    }
}