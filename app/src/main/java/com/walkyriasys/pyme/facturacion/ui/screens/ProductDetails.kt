package com.walkyriasys.pyme.facturacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.walkyriasys.pyme.facturacion.domain.database.models.Product
import com.walkyriasys.pyme.facturacion.domain.database.models.ProductType
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(onBackClick: () -> Unit = {}) {
    // Mocked Product
    val product = Product(
        uuid = "1",
        name = "Mocked Product",
        description = "This is a mocked product for demonstration purposes.",
        price = 2500,
        productType = ProductType.Physical,
        stockQuantity = 15
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = product.description ?: "No description available",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Price: ${product.price / 100.0} USD",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Product Type: ${product.productType.name}",
                style = MaterialTheme.typography.bodyLarge
            )
            if (product.productType == ProductType.Physical) {
                Text(
                    text = "Stock Quantity: ${product.stockQuantity ?: "N/A"}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailsScreenPreview() {
    PymefacturacionTheme {
        ProductDetailsScreen()
    }
}