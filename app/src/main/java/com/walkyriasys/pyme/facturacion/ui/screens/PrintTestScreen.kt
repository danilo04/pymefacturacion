package com.walkyriasys.pyme.facturacion.ui.screens

import android.Manifest
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.walkyriasys.pyme.facturacion.R
import com.walkyriasys.pyme.facturacion.ui.screens.print.PrintTestViewModel
import kotlinx.coroutines.launch
import Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintTestScreen(
    navController: NavController,
    viewModel: PrintTestViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarMessage = it
            showSnackbar = true
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Print Test") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = {
            if (showSnackbar) {
                LaunchedEffect(snackbarMessage) {
                    kotlinx.coroutines.delay(3000)
                    showSnackbar = false
                }
                SnackbarHost(
                    hostState = remember { SnackbarHostState() }.apply {
                        LaunchedEffect(snackbarMessage) {
                            showSnackbar(snackbarMessage)
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Connection Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.isConnected) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else if (uiState.hasSelectedDevice)
                        MaterialTheme.colorScheme.secondaryContainer
                    else 
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Printer Status",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (uiState.hasSelectedDevice) {
                        Text(
                            text = "Selected Device: ${uiState.selectedDeviceName ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (uiState.isConnected) 
                                MaterialTheme.colorScheme.onPrimaryContainer 
                            else 
                                MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    
                    Text(
                        text = when {
                            uiState.isConnected -> "Connected"
                            uiState.hasSelectedDevice -> "Disconnected"
                            else -> "No device selected"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = when {
                            uiState.isConnected -> MaterialTheme.colorScheme.onPrimaryContainer
                            uiState.hasSelectedDevice -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onErrorContainer
                        }
                    )
                    
                    if (!uiState.isConnected && !uiState.hasSelectedDevice) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Go to Settings to select a Bluetooth printer",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    } else if (!uiState.isConnected && uiState.hasSelectedDevice) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.connect()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text("Connect to Printer")
                        }
                    } else if (uiState.isConnected) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.disconnect()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.onError
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = "Disconnect",
                                color = MaterialTheme.colorScheme.onError
                            )
                        }
                    }
                }
            }

            // Print Buttons Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Print Functions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Print Document Button
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.printTestDocument()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = uiState.isConnected && !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        } else {
                            Icon(
                                Icons.Default.Print,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text("Print Document")
                    }

                    // Print QR Code Button
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.printQRCode("https://www.pymefacturacion.com")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.isConnected && !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Print QR Code")
                    }

                    // Print Barcode Button
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.printBarcode("123456789012")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.isConnected && !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("Print Barcode")
                    }
                }
            }

            // Instructions Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Instructions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
                        1. Make sure your Bluetooth printer is turned on
                        2. Go to Settings to select and connect to your printer
                        3. Use the Connect button above if you have a saved device
                        4. The Print Document button will print a test receipt
                        5. Other buttons will print QR codes and barcodes
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Go to Settings Button (only if no device selected)
            if (!uiState.hasSelectedDevice) {
                Button(
                    onClick = {
                        navController.navigate(Screens.PrinterSelection.route)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Select Printer in Settings")
                }
            }
        }
    }
}
