package com.walkyriasys.pyme.facturacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.walkyriasys.pyme.facturacion.domain.database.models.Product
import com.walkyriasys.pyme.facturacion.domain.database.models.ProductType
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(navController: NavController) {
    // Hardcoded list of products
    val products = listOf(
        Product(uuid = "1", name = "Product A", description = "Description A", price = 1000, stockQuantity = 10, productType = ProductType.Physical),
        Product(uuid = "2", name = "Product B", description = "Description B", price = 2000, stockQuantity = 5, productType = ProductType.Physical),
        Product(uuid = "3", name = "Product C", description = "Description C", price = 3000, stockQuantity = 2, productType = ProductType.Physical)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddProduct.route)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products) { product ->
                ProductItem(product = product)
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold
            )
            Text(text = product.description ?: "No description available")
            Text(text = "Price: ${product.price / 100.0} USD")
            Text(text = "Stock: ${product.stockQuantity ?: "N/A"}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenPreview() {
    PymefacturacionTheme {
        ProductsScreen(rememberNavController())
    }
}