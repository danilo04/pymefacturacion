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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
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
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle()

    ProductList(
        navController = navController,
        state = uiState.value,
        searchQuery = searchQuery.value,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onClearSearch = viewModel::clearSearch,
        onAddProduct = { navController.navigate(Screens.AddProduct.route) },
        onBackPressed = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductList(
    navController: NavController,
    state: ProductsViewModel.UiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
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
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProduct
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_product)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = stringResource(R.string.search_products_placeholder)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.search_icon))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(
                                onClick = onClearSearch
                            ) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = stringResource(R.string.clear_icon))
                            }
                        }
                    }
                )

                ProductList(state, searchQuery)
            }
        }
    }
}

@Composable
private fun ProductList(
    state: ProductsViewModel.UiState,
    searchQuery: String
) {
    when (state) {
        is ProductsViewModel.UiState.Loaded -> {
            val products = state.products
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(R.string.loading_products),
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        ProductsViewModel.UiState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (searchQuery.isNotEmpty()) {
                        stringResource(R.string.no_products_found, searchQuery)
                    } else {
                        stringResource(R.string.no_products_yet)
                    },
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
private fun ProductItem(product: ProductItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                        contentDescription = stringResource(R.string.product_image_description, product.name),
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
                    text = stringResource(R.string.price_format, product.price),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
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
            searchQuery = "",
            onSearchQueryChange = {},
            onClearSearch = {},
            onBackPressed = {},
            onAddProduct = {}
        )
    }
}