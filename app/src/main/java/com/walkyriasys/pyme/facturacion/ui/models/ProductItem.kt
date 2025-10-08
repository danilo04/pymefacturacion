package com.walkyriasys.pyme.facturacion.ui.models

import android.net.Uri
import com.walkyriasys.pyme.facturacion.domain.database.models.Product
import com.walkyriasys.pyme.facturacion.domain.database.models.ProductType
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

sealed class ProductItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val uuid: String,
    val picturePath: Uri?
) {
    data class PhysicalProduct(
        val productId: Int,
        val productName: String,
        val productDescription: String,
        val productPrice: BigDecimal,
        val productStock: Int,
        val productUuid: String,
        val productPicturePath: Uri? = null,
    ) : ProductItem(productId, productName, productDescription, productPrice, productUuid, productPicturePath)

    data class DigitalProduct(
        val productId: Int,
        val productName: String,
        val productDescription: String,
        val productPrice: BigDecimal,
        val productUuid: String,
        val productPicturePath: Uri? = null,
    ) : ProductItem(productId, productName, productDescription, productPrice, productUuid, productPicturePath)

    data class ServiceProduct(
        val productId: Int,
        val productName: String,
        val productDescription: String,
        val productPrice: BigDecimal,
        val productUuid: String,
        val productPicturePath: Uri? = null,
    ) : ProductItem(productId, productName, productDescription, productPrice, productUuid, productPicturePath)
}

fun productItem(
    name: String,
    description: String,
    price: String,
    stock: Int?,
    type: ProductType,
    uuid: () -> String,
    picturePath: Uri? = null,
): ProductItem {
    return when (type) {
        ProductType.Physical -> {
            ProductItem.PhysicalProduct(
                productId = 0,
                productName = name,
                productDescription = description,
                productPrice = BigDecimal(price),
                productUuid = uuid.invoke(),
                productStock = stock ?: 0,
                productPicturePath = picturePath
            )
        }

        ProductType.Digital -> {
            ProductItem.DigitalProduct(
                productId = 0,
                productName = name,
                productDescription = description,
                productPrice = BigDecimal(price),
                productUuid = uuid.invoke(),
                productPicturePath = picturePath
            )
        }

        ProductType.Service -> {
            ProductItem.ServiceProduct(
                productId = 0,
                productName = name,
                productDescription = description,
                productPrice = BigDecimal(price),
                productUuid = uuid.invoke(),
                productPicturePath = picturePath
            )
        }
    }
}

fun Product.toProductItem(): ProductItem {
    return when (productType) {
        ProductType.Physical -> ProductItem.PhysicalProduct(
            productId = id,
            productName = name,
            productDescription = description ?: "",
            productPrice = price.minorToMajorUnits(),
            productStock = stockQuantity ?: 0,
            productUuid = uuid,
            productPicturePath = picturePath?.let { Uri.parse("file://$it") },
        )

        ProductType.Digital -> ProductItem.DigitalProduct(
            productId = id,
            productName = name,
            productDescription = description ?: "",
            productPrice = price.minorToMajorUnits(),
            productUuid = uuid,
            productPicturePath = picturePath?.let { Uri.parse("file://$it") },
        )

        ProductType.Service -> ProductItem.ServiceProduct(
            productId = id,
            productName = name,
            productDescription = description ?: "",
            productPrice = price.minorToMajorUnits(),
            productUuid = uuid,
            productPicturePath = picturePath?.let { Uri.parse("file://$it") },
        )
    }
}

fun ProductItem.toProduct(productPhoto: String? = null): Product {
    return when (this) {
        is ProductItem.DigitalProduct -> Product(
            id = productId,
            uuid = productUuid,
            name = productName,
            description = productDescription,
            price = productPrice.majorToMinorUnits(),
            productType = ProductType.Digital,
            stockQuantity = null, // Digital products typically do not have stock
            picturePath = productPhoto
        )

        is ProductItem.PhysicalProduct -> Product(
            id = productId,
            uuid = productUuid,
            name = productName,
            description = productDescription,
            price = productPrice.majorToMinorUnits(),
            productType = ProductType.Physical,
            stockQuantity = productStock,
            picturePath = productPhoto
        )

        is ProductItem.ServiceProduct -> Product(
            id = productId,
            uuid = productUuid,
            name = productName,
            description = productDescription,
            price = productPrice.majorToMinorUnits(),
            productType = ProductType.Service,
            stockQuantity = null, // Services typically do not have stock
            picturePath = productPhoto
        )
    }
}

fun BigDecimal.majorToMinorUnits(currencyCode: String = "USD"): Long {
    val currency = Currency.getInstance(currencyCode)
    val minorUnitExponent = currency.defaultFractionDigits // e.g., 2 for USD, 0 for JPY, 3 for KWD
    val multiplier = BigDecimal.TEN.pow(minorUnitExponent)
    return this.setScale(minorUnitExponent, RoundingMode.HALF_UP)
        .multiply(multiplier)
        .toLong()
}

fun Long.minorToMajorUnits(currencyCode: String = "USD"): BigDecimal {
    val currency = Currency.getInstance(currencyCode)
    val minorUnitExponent = currency.defaultFractionDigits
    val divisor = BigDecimal.TEN.pow(minorUnitExponent)
    return BigDecimal(this).divide(divisor, minorUnitExponent, RoundingMode.HALF_UP)
}