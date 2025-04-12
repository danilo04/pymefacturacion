package com.walkyriasys.pyme.facturacion.ui.screens

import QuantitySelector
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.walkyriasys.pyme.facturacion.domain.database.models.ProductType
import com.walkyriasys.pyme.facturacion.ui.components.MoneyOutlinedTextField
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(onProductAdded: (String, String, Int, Int?, ProductType) -> Unit = { _, _, _, _, _ -> }) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf("") }
    var stockQuantity by remember { mutableStateOf(0) }
    var productType by remember { mutableStateOf(ProductType.Physical) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Product") }
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // Adjust height for at least two lines
                maxLines = 4 // Allow up to 4 lines
            )
            MoneyOutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (in cents)") },
            )
            // Product Type Dropdown
            Box {
                OutlinedTextField(
                    value = productType.name,
                    onValueChange = {},
                    label = { Text("Product Type") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Product Type")
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ProductType.values().forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.name) },
                            onClick = {
                                productType = type
                                expanded = false
                            }
                        )
                    }
                }
            }
            // Conditionally show stock quantity field
            if (productType == ProductType.Physical) {
//                OutlinedTextField(
//                    value = stockQuantity,
//                    onValueChange = { stockQuantity = it },
//                    label = { Text("Stock Quantity") },
//                    modifier = Modifier.fillMaxWidth(),
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        keyboardType = KeyboardType.Number
//                    )
//                )
                QuantitySelector(
                    value = stockQuantity,
                    onValueChange = { newValue ->
                        stockQuantity = newValue
                    },
                    minValue = 1,
                    maxValue = 100
                )
            }
            Button(
                onClick = {
                    val priceValue = price.toIntOrNull() ?: 0
                    val stockValue = if (productType == ProductType.Physical) stockQuantity else null
                    onProductAdded(name.text, description.text, priceValue, stockValue, productType)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Product")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    PymefacturacionTheme {
        AddProductScreen()
    }
}