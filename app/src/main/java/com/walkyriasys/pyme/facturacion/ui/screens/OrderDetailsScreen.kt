package com.walkyriasys.pyme.facturacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.ui.LocalNavigator
import com.walkyriasys.pyme.facturacion.ui.viewModels.OrderDetailsViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    navController: NavController,
    orderId: Long
) {
    val navigator = LocalNavigator.current
    val viewModel: OrderDetailsViewModel = hiltViewModel(
        key = "OrderDetailsViewModel${orderId}${navigator.hashCode()}",
        creationCallback = { factory: OrderDetailsViewModel.Factory ->
            factory.create(orderId)
        }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        // Order is now loaded automatically in the ViewModel's init block
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is OrderDetailsViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is OrderDetailsViewModel.UiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            is OrderDetailsViewModel.UiState.Success -> {
                OrderDetailsContent(
                    order = state.order,
                    orderItems = state.orderItems,
                    onStatusUpdate = viewModel::updateOrderStatus,
                    onPrintReceipt = viewModel::printReceipt,
                    message = state.message,
                    onMessageDismissed = viewModel::clearMessage,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun OrderDetailsContent(
    order: Order,
    orderItems: List<com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem>,
    onStatusUpdate: (Order.OrderStatus) -> Unit,
    onPrintReceipt: () -> Unit,
    message: String?,
    onMessageDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Show message if there is one
    message?.let { msg ->
        LaunchedEffect(msg) {
            kotlinx.coroutines.delay(3000) // Show message for 3 seconds
            onMessageDismissed()
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Show message at the top if there is one
        message?.let { msg ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (msg.contains("successfully")) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = msg,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        // Order Information Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Order Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Order ID:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "#${order.id}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Customer:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = order.customerName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Amount:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "$${order.totalAmount / 100.0}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Created:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = order.createdAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                order.expectedDeliveryDate?.let { deliveryDate ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Expected Delivery:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = deliveryDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a")),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Order Status Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Order Status",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // Current Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Status:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    StatusChip(status = order.orderStatus)
                }
                
                // Status Update Buttons
                when (order.orderStatus) {
                    Order.OrderStatus.PENDING -> {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onStatusUpdate(Order.OrderStatus.COMPLETED) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Mark as Completed")
                            }
                            
                            OutlinedButton(
                                onClick = onPrintReceipt,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Default.Print, 
                                    contentDescription = "Print Receipt",
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text("Print Receipt")
                            }
                        }
                    }
                    Order.OrderStatus.COMPLETED -> {
                        Button(
                            onClick = { onStatusUpdate(Order.OrderStatus.DELIVERED) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Mark as Delivered")
                        }
                    }
                    Order.OrderStatus.DELIVERED -> {
                        Text(
                            text = "Order has been delivered",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Order Items Card
        if (orderItems.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Order Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    orderItems.forEach { item ->
                        OrderItemRow(item = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusChip(status: Order.OrderStatus) {
    val (backgroundColor, contentColor) = when (status) {
        Order.OrderStatus.PENDING -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        Order.OrderStatus.COMPLETED -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        Order.OrderStatus.DELIVERED -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
    }
    
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun OrderItemRow(item: com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Product ID: ${item.productId}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Quantity: ${item.quantity}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Text(
            text = "$${item.price / 100.0}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
