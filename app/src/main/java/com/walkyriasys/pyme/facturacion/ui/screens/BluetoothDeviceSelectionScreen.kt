package com.walkyriasys.pyme.facturacion.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.walkyriasys.pyme.facturacion.domain.models.BluetoothDevice
import com.walkyriasys.pyme.facturacion.ui.LocalNavigator
import com.walkyriasys.pyme.facturacion.ui.theme.PymefacturacionTheme
import com.walkyriasys.pyme.facturacion.ui.viewModels.BluetoothDeviceSelectionViewModel

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothDeviceSelectionScreen(navController: NavController) {
    val navigator = LocalNavigator.current
    val viewModel: BluetoothDeviceSelectionViewModel = hiltViewModel(
        key = "BluetoothDeviceSelectionViewModel${navigator.hashCode()}",
        creationCallback = { factory: BluetoothDeviceSelectionViewModel.Factory ->
            factory.create(navigator)
        }
    )

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val selectedDevice by viewModel.selectedDevice.collectAsStateWithLifecycle()

    // Permission launcher for Bluetooth
    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val bluetoothConnectGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
        if (bluetoothConnectGranted) {
            viewModel.refreshDevices()
        }
    }

    // Request permissions on screen load
    LaunchedEffect(Unit) {
        bluetoothPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Bluetooth Printer") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is BluetoothDeviceSelectionViewModel.UiState.DevicesLoaded) {
                        IconButton(onClick = { viewModel.refreshDevices() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (uiState) {
                is BluetoothDeviceSelectionViewModel.UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is BluetoothDeviceSelectionViewModel.UiState.BluetoothNotSupported -> {
                    BluetoothNotSupportedContent()
                }

                is BluetoothDeviceSelectionViewModel.UiState.BluetoothDisabled -> {
                    BluetoothDisabledContent()
                }

                is BluetoothDeviceSelectionViewModel.UiState.NoPairedDevices -> {
                    NoPairedDevicesContent()
                }

                is BluetoothDeviceSelectionViewModel.UiState.DevicesLoaded -> {
                    DeviceListContent(
                        devices = uiState.devices,
                        selectedDevice = selectedDevice,
                        onDeviceSelected = viewModel::selectDevice,
                        onClearSelection = if (selectedDevice != null) {
                            { viewModel.clearSelectedDevice() }
                        } else null
                    )
                }

                is BluetoothDeviceSelectionViewModel.UiState.Error -> {
                    ErrorContent(
                        message = uiState.message,
                        onRetry = { viewModel.refreshDevices() }
                    )
                }
            }
        }
    }
}

@Composable
private fun BluetoothNotSupportedContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.BluetoothDisabled,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = "Bluetooth is not supported on this device",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun BluetoothDisabledContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.BluetoothDisabled,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = "Bluetooth is disabled",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Please enable Bluetooth to scan for printers",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun NoPairedDevicesContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Bluetooth,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "No paired Bluetooth devices",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Please pair your Bluetooth printer in device settings first",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun DeviceListContent(
    devices: List<BluetoothDevice>,
    selectedDevice: BluetoothDevice?,
    onDeviceSelected: (BluetoothDevice) -> Unit,
    onClearSelection: (() -> Unit)?
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Show current selection if any
        selectedDevice?.let { selected ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Current Selection",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = selected.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = selected.address,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        onClearSelection?.let { clearAction ->
                            OutlinedButton(
                                onClick = clearAction,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Clear Selection")
                            }
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Available Paired Devices",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(devices) { device ->
            BluetoothDeviceItem(
                device = device,
                isSelected = selectedDevice?.address == device.address,
                onSelected = { onDeviceSelected(device) }
            )
        }
    }
}

@Composable
private fun BluetoothDeviceItem(
    device: BluetoothDevice,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() }
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
                    Icons.Default.Bluetooth,
                    contentDescription = "Bluetooth Device",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = device.address,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            RadioButton(
                selected = isSelected,
                onClick = onSelected
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error loading devices",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceListContentPreview() {
    PymefacturacionTheme {
        val mockDevices = listOf(
            BluetoothDevice(
                name = "Bluetooth Printer 1",
                address = "00:11:22:33:44:55",
                isConnected = false
            ),
            BluetoothDevice(
                name = "Thermal Printer",
                address = "AA:BB:CC:DD:EE:FF",
                isConnected = false
            ),
            BluetoothDevice(
                name = "POS Printer",
                address = "12:34:56:78:90:AB",
                isConnected = false
            )
        )
        
        val selectedDevice = BluetoothDevice(
            name = "Thermal Printer",
            address = "AA:BB:CC:DD:EE:FF",
            isConnected = false
        )
        
        DeviceListContent(
            devices = mockDevices,
            selectedDevice = selectedDevice,
            onDeviceSelected = { },
            onClearSelection = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoPairedDevicesContentPreview() {
    PymefacturacionTheme {
        NoPairedDevicesContent()
    }
}
