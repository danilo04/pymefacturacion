package com.walkyriasys.pyme.facturacion.ui.screens

import Screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.walkyriasys.pyme.facturacion.ui.viewModels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onPrinterSelected: (String) -> Unit,
    onBarcodeGenerationChanged: (Boolean) -> Unit
) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val selectedPrinterName by viewModel.selectedPrinterName.collectAsStateWithLifecycle()
    var isBarcodeGenerationEnabled by remember { mutableStateOf(false) }

    // Refresh printer selection when returning to this screen
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refreshSelectedPrinter()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Connect Printer Option
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clickable {
                        navController.navigate(Screens.PrinterSelection.route)
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Print,
                            contentDescription = "Printer",
                            modifier = Modifier.size(24.dp)
                        )
                        Column(
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text("Connect Printer")
                            selectedPrinterName?.let { printerName ->
                                Text(
                                    text = "Selected: $printerName",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } ?: Text(
                                text = "No printer selected",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Navigate forward",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Barcode Generation Option
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.QrCode,
                            contentDescription = "Barcode",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Enable Barcode Generation",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    Switch(
                        checked = isBarcodeGenerationEnabled,
                        onCheckedChange = {
                            isBarcodeGenerationEnabled = it
                            onBarcodeGenerationChanged(it)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        navController = NavController(LocalContext.current),
        onPrinterSelected = { /* No-op in preview */ },
        onBarcodeGenerationChanged = { /* No-op in preview */ }
    )
}