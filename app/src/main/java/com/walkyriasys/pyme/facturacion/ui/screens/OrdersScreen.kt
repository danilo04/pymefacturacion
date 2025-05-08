package com.walkyriasys.pyme.facturacion.ui.screens

import Screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.Order.OrderStatus
import com.walkyriasys.pyme.facturacion.ui.components.OnBottomReached
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme
import com.walkyriasys.pyme.facturacion.ui.viewModels.OrdersViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val viewModel = hiltViewModel<OrdersViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle(OrdersViewModel.UiState.Loading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Orders",
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.AddOrder.route) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Order"
                )
            }
        }
    ) { paddingValues ->
        when (val state = uiState.value) {
            OrdersViewModel.UiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "There are no orders.",
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }
            is OrdersViewModel.UiState.Loaded -> {
                OrdersList(state, paddingValues)
            }
            OrdersViewModel.UiState.Loading -> {
            }
        }
    }
}

@Composable
private fun OrdersList(state: OrdersViewModel.UiState.Loaded, paddingValues: PaddingValues) {
    val orders = state.orders
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(orders.size) { index ->
            val order = orders[index]
            OrderRow(order)
        }
    }

    listState.OnBottomReached(10) {
        state.loadMore.invoke()
    }
}

@Composable
private fun OrderRow(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        BasicText(
            text = "Order ID: ${order.id}",
            style = MaterialTheme.typography.bodyLarge
        )
        BasicText(
            text = "Status: ${order.orderStatus}",
            style = MaterialTheme.typography.bodyMedium
        )
        BasicText(
            text = "Total Amount: ${order.totalAmount / 100.0} USD",
            style = MaterialTheme.typography.bodyMedium
        )
        BasicText(
            text = "Created At: ${order.createdAt}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrdersScreenPreview() {
    PymefacturacionTheme {
        val state = OrdersViewModel.UiState.Loaded(
            listOf(
                Order(
                    id = 1,
                    orderStatus = OrderStatus.COMPLETED,
                    totalAmount = 15000,
                    createdAt = LocalDate.now().minusDays(1),
                    customerName = "John Doe"
                ),
                Order(
                    id = 2,
                    orderStatus = OrderStatus.PENDING,
                    totalAmount = 25000,
                    createdAt = LocalDate.now().minusDays(2),
                    customerName = "John Doe"
                ),
                Order(
                    id = 3,
                    orderStatus = OrderStatus.DELIVERED,
                    totalAmount = 10000,
                    createdAt = LocalDate.now().minusDays(3),
                    customerName = "John Doe"
                ))
        ) {}
        OrdersList(state, PaddingValues(0.dp))
    }
}