package com.walkyriasys.pyme.facturacion.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem
import com.walkyriasys.pyme.facturacion.domain.repositories.OrdersRepository
import com.walkyriasys.pyme.facturacion.domain.print.PrinterService
import com.walkyriasys.pyme.facturacion.domain.print.BluetoothConnectionManager
import com.walkyriasys.pyme.facturacion.domain.preferences.BluetoothPreferencesManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@HiltViewModel(assistedFactory = OrderDetailsViewModel.Factory::class)
class OrderDetailsViewModel @AssistedInject constructor(
    private val ordersRepository: OrdersRepository,
    private val printerService: PrinterService,
    private val bluetoothConnectionManager: BluetoothConnectionManager,
    private val bluetoothPreferencesManager: BluetoothPreferencesManager,
    @Assisted private val orderId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(orderId: Long): OrderDetailsViewModel
    }

    init {
        loadOrder()
    }

    private fun loadOrder() {
        viewModelScope.launch {
            try {
                val order = ordersRepository.getOrderWithItems(orderId)
                if (order != null) {
                    val orderItems = ordersRepository.getOrderItems(orderId)
                    _uiState.value = UiState.Success(
                        order = order,
                        orderItems = orderItems
                    )
                } else {
                    _uiState.value = UiState.Error("Order not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to load order: ${e.message}")
            }
        }
    }

    fun updateOrderStatus(newStatus: Order.OrderStatus) {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            viewModelScope.launch {
                try {
                    val updatedOrder = currentState.order.copy(orderStatus = newStatus)
                    ordersRepository.updateOrder(updatedOrder)
                    _uiState.value = currentState.copy(order = updatedOrder)
                } catch (e: Exception) {
                    // Handle error, maybe show a snackbar
                    // For now, we'll just log the error
                }
            }
        }
    }

    fun printReceipt() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            viewModelScope.launch {
                try {
                    val order = currentState.order
                    
                    // Check if printer is connected, if not, try to connect
                    if (!bluetoothConnectionManager.isConnected()) {
                        val savedAddress = bluetoothPreferencesManager.getSelectedBluetoothDeviceAddress()
                        
                        if (savedAddress == null) {
                            _uiState.value = currentState.copy(
                                message = "No printer selected. Please go to Settings to select a printer first."
                            )
                            return@launch
                        }
                        
                        // Try to connect to the printer
                        _uiState.value = currentState.copy(
                            message = "Connecting to printer..."
                        )
                        
                        val connectionResult = bluetoothConnectionManager.connect(savedAddress)
                        when (connectionResult) {
                            is BluetoothConnectionManager.BluetoothConnectionResult.Connected -> {
                                // Connection successful, continue with printing
                            }
                            is BluetoothConnectionManager.BluetoothConnectionResult.BluetoothDisabled -> {
                                _uiState.value = currentState.copy(
                                    message = "Bluetooth is disabled. Please enable Bluetooth and try again."
                                )
                                return@launch
                            }
                            is BluetoothConnectionManager.BluetoothConnectionResult.InvalidAddress -> {
                                _uiState.value = currentState.copy(
                                    message = "Invalid printer address. Please select a different printer."
                                )
                                return@launch
                            }
                            is BluetoothConnectionManager.BluetoothConnectionResult.Connecting -> {
                                _uiState.value = currentState.copy(
                                    message = "Printer is already connecting. Please wait..."
                                )
                                return@launch
                            }
                            is BluetoothConnectionManager.BluetoothConnectionResult.ErrorConnecting -> {
                                _uiState.value = currentState.copy(
                                    message = "Failed to connect to printer: ${connectionResult.exception?.message ?: "Unknown error"}"
                                )
                                return@launch
                            }
                        }
                    }
                    
                    // Create QR code data in the specified format:
                    // ORDER_ID|CUSTOMER_NAME|DATE|DELIVERY_DATE|BUSINESS_NAME
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val deliveryDateStr = order.expectedDeliveryDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "N/A"
                    
                    val qrData = "${order.id}|${order.customerName}|${order.createdAt.format(dateFormatter)}|$deliveryDateStr|Lavanderia Walki"
                    
                    val result = printerService.printQRCode(qrData)
                    if (result.isSuccess) {
                        _uiState.value = currentState.copy(
                            message = "Receipt printed successfully!"
                        )
                    } else {
                        _uiState.value = currentState.copy(
                            message = "Failed to print receipt: ${result.exceptionOrNull()?.message}"
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = currentState.copy(
                        message = "Error printing receipt: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearMessage() {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            _uiState.value = currentState.copy(message = null)
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data class Error(val message: String) : UiState
        data class Success(
            val order: Order,
            val orderItems: List<OrderItem>,
            val message: String? = null
        ) : UiState
    }
}
