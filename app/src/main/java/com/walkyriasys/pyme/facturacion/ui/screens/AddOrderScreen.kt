package com.walkyriasys.pyme.facturacion.ui.screens

import QuantitySelector
import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import com.walkyriasys.pyme.facturacion.ui.components.DatetimePicker
import com.walkyriasys.pyme.facturacion.ui.models.OrderItemEntry
import com.walkyriasys.pyme.facturacion.ui.models.ProductItem
import com.walkyriasys.pyme.facturacion.ui.models.majorToMinorUnits
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme
import com.walkyriasys.pyme.facturacion.ui.viewModels.AddEditOrderViewModel
import com.walkyriasys.pyme.facturacion.ui.viewModels.ProductsViewModel
import com.walkyriasys.pyme.utils.toMoneyFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
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
    var customerName by remember { mutableStateOf("") }
    // var totalAmount by remember { mutableStateOf("0") }

    // Order date picker state
    val orderDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    )
    var showOrderDatePicker by remember { mutableStateOf(false) }
    val selectedOrderDate by remember {
        derivedStateOf {
            orderDatePickerState.selectedDateMillis?.let {
                Instant.ofEpochMilli(it)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ISO_DATE)
            } ?: LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        }
    }

    // Initialize with delivery date 7 days in the future
    val initialDateTime = LocalDateTime.now().plusDays(7)
    var selectedDateTime by remember { mutableStateOf(initialDateTime) }
    
    // Order items state
    val orderItems = remember { mutableStateListOf<OrderItemEntry>() }

    val totalAmount by remember {
        derivedStateOf {
            orderItems.sumOf { item ->
                val price = item.price
                val total = price * item.quantity.toBigDecimal()
                Log.i("Test", "Total $total for item ${item.productName}")
                total
            }.toMoneyFormat()
        }
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
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Total amount display
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Amount:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = totalAmount,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    // Submit button
                    Button(
                        onClick = {
                            val orderDate = try {
                                LocalDate.parse(selectedOrderDate)
                            } catch (_: Exception) {
                                LocalDate.now()
                            }

                            viewModel.addOrder(
                                order = Order(
                                    orderStatus = Order.OrderStatus.PENDING, // Default to PENDING
                                    expectedDeliveryDate = selectedDateTime, // Use the datetime from our picker
                                    totalAmount = totalAmount.toIntOrNull() ?: 0,
                                    customerName = customerName,
                                    createdAt = orderDate
                                ),
                                orderItems = orderItems.map { item ->
                                    OrderItem(
                                        id = 0,
                                        uuid = viewModel.genNewUuid(),
                                        orderId = 0, // This will be set by the viewModel
                                        productId = item.productId,
                                        quantity = item.quantity,
                                        price = item.price.majorToMinorUnits(),
                                        discount = null
                                    )
                                }
                            )
                        },
                        enabled = orderItems.isNotEmpty()
                                && customerName.isNotEmpty() && selectedDateTime != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Order")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Customer name
            item {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Customer Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            // Order Date
            item {
                OutlinedTextField(
                    value = selectedOrderDate,
                    onValueChange = {},
                    label = { Text("Order Date") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showOrderDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Select order date"
                            )
                        }
                    }
                )

                if (showOrderDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showOrderDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = { showOrderDatePicker = false }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showOrderDatePicker = false }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = orderDatePickerState)
                    }
                }
            }

            // Expected delivery date
            item {
                DatetimePicker(
                    initialDateTime = LocalDateTime.now(),
                    formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a"),
                    onDateTimeSelected = { selectedDateTime = it },
                    label = "Delivery Date and Time"
                )
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
                                                            price = product.price,
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
                    text = "Price: $${item.price}",
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
                text = "$${product.price}",
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

@Preview(showBackground = true)
@Composable
fun AddOrderScreenPreview() {
    PymefacturacionTheme {
        AddOrderScreen(rememberNavController())
    }
}