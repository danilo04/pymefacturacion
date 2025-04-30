package com.walkyriasys.pyme.facturacion.ui.screens

import QuantitySelector
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem
import com.walkyriasys.pyme.facturacion.ui.LocalNavigator
import com.walkyriasys.pyme.facturacion.ui.models.ProductItem
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme
import com.walkyriasys.pyme.facturacion.ui.viewModels.AddEditOrderViewModel
import com.walkyriasys.pyme.facturacion.ui.viewModels.ProductsViewModel
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    navController: NavController
) {
    val navigator = LocalNavigator.current
    val viewModel: AddEditOrderViewModel = hiltViewModel(
        key = "AddEditOrderViewModel${navigator.hashCode()}",
        creationCallback = { factory: AddEditOrderViewModel.Factory ->
            factory.create(navigator)
        }
    )

    // Load products to select
    val productsViewModel = hiltViewModel<ProductsViewModel>()
    val productsState =
        productsViewModel.uiState.collectAsStateWithLifecycle(ProductsViewModel.UiState.Loading)

    // Order states
    var selectedStatus by remember { mutableStateOf(Order.Status.PENDING) }
    var statusExpanded by remember { mutableStateOf(false) }
    var totalAmount by remember { mutableStateOf("0") }

    // Date picker state
    val initialDateMillis = LocalDate.now().plusDays(7)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant().toEpochMilli()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDate by remember {
        derivedStateOf {
            datePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ISO_DATE)
            } ?: LocalDate.now().plusDays(7).format(DateTimeFormatter.ISO_DATE)
        }
    }

    // Order items state
    val orderItems = remember { mutableStateListOf<OrderItemEntry>() }

    // Calculate total as items change
    LaunchedEffect(orderItems) {
        totalAmount = orderItems.sumOf { it.quantity * it.price.toLong() }.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Order") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status selector
            item {
                Text(
                    text = "Order Status",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {
                    TextField(
                        value = selectedStatus.name,
                        onValueChange = {},
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                        readOnly = true,
                        label = { Text("Status") },
                        trailingIcon = { TrailingIcon(expanded = statusExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        Order.Status.entries.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.name) },
                                onClick = {
                                    selectedStatus = status
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Expected delivery date
            item {
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = {},
                    label = { Text("Expected Delivery Date") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Select date"
                            )
                        }
                    }
                )

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }

            // Products selection header
            item {
                Text(
                    text = "Order Items",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Show selected items
            items(orderItems) { item ->
                OrderItemCard(
                    item = item,
                    onDelete = { orderItems.remove(item) },
                    onQuantityChange = { newQuantity ->
                        val index = orderItems.indexOf(item)
                        if (index >= 0) {
                            orderItems[index] = item.copy(quantity = newQuantity)
                        }
                    }
                )
            }

            // Product selection section
            item {
                when (val state = productsState.value) {
                    is ProductsViewModel.UiState.Loaded -> {
                        val products = state.products

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Add Products to Order",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                ) {
                                    items(products) { product ->
                                        ProductSelectionRow(
                                            product = product,
                                            onAddClick = {
                                                // Check if already added
                                                val existingItem =
                                                    orderItems.find { it.productId == product.id }
                                                if (existingItem != null) {
                                                    // Increment quantity
                                                    val index = orderItems.indexOf(existingItem)
                                                    orderItems[index] =
                                                        existingItem.copy(quantity = existingItem.quantity + 1)
                                                } else {
                                                    // Add new item
                                                    orderItems.add(
                                                        OrderItemEntry(
                                                            productId = product.id,
                                                            productName = product.name,
                                                            price = product.price.toString(),
                                                            quantity = 1
                                                        )
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    ProductsViewModel.UiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Loading products...")
                        }
                    }

                    ProductsViewModel.UiState.Empty -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No products available. Please add products first.")
                        }
                    }
                }
            }

            // Total amount display
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Amount:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$${BigDecimal(if (totalAmount.isEmpty()) "0" else totalAmount).toDouble() / 100} USD",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            // Submit button
            item {
                Button(
                    onClick = {
                        val deliveryDate = try {
                            val date = LocalDate.parse(selectedDate)
                            LocalDateTime.of(date, LocalTime.NOON)
                        } catch (e: Exception) {
                            LocalDateTime.now().plusDays(7)
                        }

                        viewModel.addOrder(
                            order = Order(
                                status = selectedStatus,
                                expectedDeliveryDate = deliveryDate,
                                totalAmount = totalAmount.toIntOrNull() ?: 0
                            ),
                            orderItems = orderItems.map { item ->
                                OrderItem(
                                    id = 0,
                                    uuid = viewModel.genNewUuid(),
                                    orderId = 0, // This will be set by the viewModel
                                    productId = item.productId,
                                    quantity = item.quantity,
                                    price = item.price.toIntOrNull() ?: 0,
                                    discount = null
                                )
                            }
                        )
                    },
                    enabled = orderItems.isNotEmpty() && (totalAmount.toIntOrNull() ?: 0) > 0,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Order")
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(
    item: OrderItemEntry,
    onDelete: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = item.productName, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Price: $${BigDecimal(item.price).toDouble() / 100} USD",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            QuantitySelector(
                label = "",
                value = item.quantity,
                onValueChange = onQuantityChange,
                minValue = 1
            )

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove item"
                )
            }
        }
    }
}

@Composable
fun ProductSelectionRow(
    product: ProductItem,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$${product.price} USD",
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = onAddClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add to order"
            )
        }
    }
}

// Helper data class for UI state
data class OrderItemEntry(
    val productId: Int,
    val productName: String,
    val price: String,
    val quantity: Int
)

@Preview(showBackground = true)
@Composable
fun AddOrderScreenPreview() {
    PymefacturacionTheme {
        AddOrderScreen(rememberNavController())
    }
}