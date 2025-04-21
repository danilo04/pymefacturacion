package com.walkyriasys.pyme.facturacion.ui.screens

import Screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.walkyriasys.pyme.facturacion.R
import com.walkyriasys.pyme.facturacion.ui.components.OnBottomReached
import com.walkyriasys.pyme.facturacion.ui.models.ProductItem
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme
import com.walkyriasys.pyme.facturacion.ui.viewModels.ProductsViewModel
import java.math.BigDecimal

@Composable
fun ProductsScreen(navController: NavController) {
    val viewModel = hiltViewModel<ProductsViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle(ProductsViewModel.UiState.Loading)

    ProductList(uiState.value) {
        navController.navigate(Screens.AddProduct.route)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductList(state: ProductsViewModel.UiState, onAddProduct: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.products_title)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProduct
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_product))
            }
        }
    ) { paddingValues ->
        when (state) {
            is ProductsViewModel.UiState.Loaded -> {
                val products = state.products
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
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

                listState.OnBottomReached(10) {
                    state.loadMore()
                }
            }

            ProductsViewModel.UiState.Loading -> {

            }
        }
    }
}


@Composable
private fun ProductItem(product: ProductItem) {
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
            Text(text = product.description)
            Text(text = "Price: ${product.price} USD")
//            Text(text = "Stock: ${product.stockQuantity ?: "N/A"}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenPreview() {
    PymefacturacionTheme {
        val mockProducts = listOf(
            ProductItem.DigitalProduct(
                productName = "Product 1",
                productDescription = "Description 1",
                productPrice = BigDecimal(1000),
                productId = 1,
                productUuid = "132"
            ),
            ProductItem.ServiceProduct(
                productName = "Product 2",
                productDescription = "Description 2",
                productPrice = BigDecimal(2000.34),
                productId = 2,
                productUuid = "434"
            ),
            ProductItem.PhysicalProduct(
                productName = "Product 3",
                productDescription = "Description 3",
                productPrice = BigDecimal(345.43),
                productId = 3,
                productStock = 5,
                productUuid = "667"
            )
        )

        ProductList(
            state = ProductsViewModel.UiState.Loaded(products = mockProducts) {},
            onAddProduct = {}
        )
    }
}