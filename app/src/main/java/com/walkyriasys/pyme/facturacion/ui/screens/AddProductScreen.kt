package com.walkyriasys.pyme.facturacion.ui.screens

import QuantitySelector
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.walkyriasys.pyme.facturacion.domain.database.models.ProductType
import com.walkyriasys.pyme.facturacion.ui.LocalNavigator
import com.walkyriasys.pyme.facturacion.ui.components.ImagePicker
import com.walkyriasys.pyme.facturacion.ui.components.MoneyOutlinedTextField
import com.walkyriasys.pyme.facturacion.ui.models.productItem
import android.net.Uri
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme
import com.walkyriasys.pyme.facturacion.ui.viewModels.AddEditProductViewModel
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavHostController
) {
    val navigator = LocalNavigator.current
    val viewModel: AddEditProductViewModel = hiltViewModel(
        key = "AddEditProductViewModel${navigator.hashCode()}",
        creationCallback = { factory: AddEditProductViewModel.Factory ->
            factory.create(navigator)
        }
    )

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf("") }
    var stockQuantity by remember { mutableStateOf(0) }
    var productType by remember { mutableStateOf(ProductType.Physical) }
    var expanded by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Product") },
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
            
            ImagePicker(
                selectedImageUri = selectedImageUri,
                onImageSelected = { uri -> selectedImageUri = uri },
                onImageRemoved = { selectedImageUri = null },
                label = "Product Image"
            )
            // Product Type Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = productType.name,
                    onValueChange = {},
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    readOnly = true,
                    label = { Text("Product Type") },
                    trailingIcon = { TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ProductType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(stringResource(type.label)) },
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
                QuantitySelector(
                    label = "In Stock",
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
                    val stockValue = if (productType == ProductType.Physical) stockQuantity else null
                    val productItem = productItem(
                        name = name.text,
                        description = description.text,
                        price = price,
                        type = productType,
                        stock = stockValue,
                        uuid = viewModel::genNewUuid,
                        picturePath = selectedImageUri
                    )

                    viewModel.addProduct(productItem)
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
        AddProductScreen(rememberNavController())
    }
}