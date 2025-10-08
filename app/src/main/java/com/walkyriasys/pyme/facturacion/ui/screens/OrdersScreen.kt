package com.walkyriasys.pyme.facturacion.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.Order.OrderStatus
import com.walkyriasys.pyme.facturacion.ui.Screens
import com.walkyriasys.pyme.facturacion.ui.components.OnBottomReached
import com.walkyriasys.pyme.facturacion.ui.components.PymeScaffold
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme
import com.walkyriasys.pyme.facturacion.ui.viewModels.OrdersViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val viewModel = hiltViewModel<OrdersViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle(OrdersViewModel.UiState.Loading)
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    
    var showFilterMenu by remember { mutableStateOf(false) }

    PymeScaffold(
        title = "Orders",
        navController = navController,
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                // TODO: Implement QR code functionality
            }) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "Read QR Code"
                )
            }
            IconButton(onClick = { showFilterMenu = true }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter Orders"
                )
            }
            DropdownMenu(
                expanded = showFilterMenu,
                onDismissRequest = { showFilterMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All Orders") },
                    onClick = {
                        viewModel.setFilter(null)
                        showFilterMenu = false
                    },
                    leadingIcon = if (selectedFilter == null) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
                DropdownMenuItem(
                    text = { Text("Pending") },
                    onClick = {
                        viewModel.setFilter(OrderStatus.PENDING)
                        showFilterMenu = false
                    },
                    leadingIcon = if (selectedFilter == OrderStatus.PENDING) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
                DropdownMenuItem(
                    text = { Text("Completed") },
                    onClick = {
                        viewModel.setFilter(OrderStatus.COMPLETED)
                        showFilterMenu = false
                    },
                    leadingIcon = if (selectedFilter == OrderStatus.COMPLETED) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
                DropdownMenuItem(
                    text = { Text("Delivered") },
                    onClick = {
                        viewModel.setFilter(OrderStatus.DELIVERED)
                        showFilterMenu = false
                    },
                    leadingIcon = if (selectedFilter == OrderStatus.DELIVERED) {
                        { Icon(Icons.Default.Check, contentDescription = null) }
                    } else null
                )
            }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter indicator
            selectedFilter?.let { filter ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Filtered by: ${filter.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextButton(
                            onClick = { viewModel.setFilter(null) }
                        ) {
                            Text("Clear Filter")
                        }
                    }
                }
            }
            
            when (val state = uiState.value) {
                OrdersViewModel.UiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val filterName = selectedFilter?.name
                            Text(
                                text = if (filterName != null) {
                                    "No orders found with status: $filterName"
                                } else {
                                    "There are no orders."
                                },
                                fontWeight = FontWeight.Bold,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                            if (selectedFilter != null) {
                                TextButton(
                                    onClick = { viewModel.setFilter(null) }
                                ) {
                                    Text("Show All Orders")
                                }
                            }
                        }
                    }
                }
                is OrdersViewModel.UiState.Loaded -> {
                    OrdersList(
                        state = state,
                        onOrderClick = { order ->
                            navController.navigate("${Screens.OrderDetail.route}/${order.id}")
                        }
                    )
                }
                OrdersViewModel.UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun OrdersList(
    state: OrdersViewModel.UiState.Loaded,
    onOrderClick: (Order) -> Unit
) {
    val orders = state.orders
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(orders.size) { index ->
            val order = orders[index]
            OrderRow(
                order = order,
                onClick = { onOrderClick(order) }
            )
        }
    }

    listState.OnBottomReached(10) {
        state.loadMore.invoke()
    }
}

@Composable
private fun OrderRow(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                StatusChip(status = order.orderStatus)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Customer: ${order.customerName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Created: ${order.createdAt}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "$${order.totalAmount / 100.0}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun StatusChip(status: OrderStatus) {
    val (backgroundColor, contentColor) = when (status) {
        OrderStatus.PENDING -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        OrderStatus.COMPLETED -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        OrderStatus.DELIVERED -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
    }
    
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
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
                    customerName = "Jane Smith"
                ),
                Order(
                    id = 3,
                    orderStatus = OrderStatus.DELIVERED,
                    totalAmount = 10000,
                    createdAt = LocalDate.now().minusDays(3),
                    customerName = "Bob Johnson"
                )
            ),
            selectedFilter = null
        ) {}
        OrdersList(state) { }
    }
}