package com.walkyriasys.pyme.facturacion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.walkyriasys.pyme.facturacion.R
import com.walkyriasys.pyme.facturacion.ui.Screens
import com.walkyriasys.pyme.facturacion.ui.components.OnBottomReached
import com.walkyriasys.pyme.facturacion.ui.components.PymeScaffold
import com.walkyriasys.pyme.facturacion.ui.models.ProductItem
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme
import com.walkyriasys.pyme.facturacion.ui.viewModels.ProductsViewModel
import java.math.BigDecimal

@Composable
fun ProductsScreen(navController: NavController) {
    val viewModel = hiltViewModel<ProductsViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle(ProductsViewModel.UiState.Loading)

    ProductList(
        navController = navController,
        state = uiState.value,
        onAddProduct = { navController.navigate(Screens.AddProduct.route) },
        onBackPressed = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductList(
    navController: NavController,
    state: ProductsViewModel.UiState,
    onAddProduct: () -> Unit,
    onBackPressed: () -> Unit,
) {
    PymeScaffold(
        title = stringResource(R.string.products_title),
        navController = navController,
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
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

            ProductsViewModel.UiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "There are no products yet.",
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
        }
    }
}


@Composable
private fun ProductItem(product: ProductItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            if (product.picturePath != null) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.picturePath)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Product image for ${product.name}",
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_product_placeholder),
                        error = painterResource(R.drawable.ic_product_placeholder),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                if (product.description.isNotBlank()) {
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
                Text(
                    text = "Price: ${product.price} USD",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
//            Text(text = "Stock: ${product.stockQuantity ?: "N/A"}")
            }
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
                productUuid = "132",
                productPicturePath = null
            ),
            ProductItem.ServiceProduct(
                productName = "Product 2",
                productDescription = "Description 2",
                productPrice = BigDecimal(2000.34),
                productId = 2,
                productUuid = "434",
                productPicturePath = null
            ),
            ProductItem.PhysicalProduct(
                productName = "Product 3",
                productDescription = "Description 3",
                productPrice = BigDecimal(345.43),
                productId = 3,
                productStock = 5,
                productUuid = "667",
                productPicturePath = null
            )
        )

        ProductList(
            navController = rememberNavController(),
            state = ProductsViewModel.UiState.Loaded(products = mockProducts) {},
            onBackPressed = {},
            onAddProduct = {}
        )
    }
}