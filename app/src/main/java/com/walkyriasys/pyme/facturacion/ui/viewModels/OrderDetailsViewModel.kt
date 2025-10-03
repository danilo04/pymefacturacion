package com.walkyriasys.pyme.facturacion.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem
import com.walkyriasys.pyme.facturacion.domain.repositories.OrdersRepository
import com.walkyriasys.pyme.facturacion.domain.print.BluetoothConnectionManager
import com.walkyriasys.pyme.facturacion.domain.preferences.BluetoothPreferencesManager
import com.walkyriasys.pyme.facturacion.domain.print.DocumentPrinter
import com.walkyriasys.pyme.facturacion.domain.print.PrintResult
import com.walkyriasys.pyme.facturacion.domain.print.documents.QRCodeReceiptDocument
import com.walkyriasys.pyme.facturacion.domain.print.drivers.PrinterDriver
import com.walkyriasys.pyme.facturacion.domain.print.drivers.netum.NetumPrinterDriver
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
    private val netumPrinterDriver: NetumPrinterDriver,
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
                    val orderItems = currentState.orderItems

                    
                    // Create QRCodeReceiptDocument
                    val qrCodeDocument = QRCodeReceiptDocument(
                        order = order,
                        orderItems = orderItems,
                        contactPhone = "123-456-7890", // Add your business phone
                        contactEmail = "info@lavanderiawalki.com" // Add your business email
                    )
                    
                    // Print using DocumentPrinter
                    val result = DocumentPrinter.print(
                        printer = netumPrinterDriver,
                        documentToPrint = qrCodeDocument
                    )
                    
                    when {
                        result.isSuccess -> {
                            _uiState.value = currentState.copy(
                                message = "Receipt printed successfully!"
                            )
                        }
                        else -> {
                            val errorMessage = when (result) {
                                is com.walkyriasys.pyme.facturacion.domain.print.PrintResult.ValidationFailed -> 
                                    "Document validation failed: ${result.reason}"
                                is com.walkyriasys.pyme.facturacion.domain.print.PrintResult.ConnectionFailed -> 
                                    "Connection failed: ${result.reason}"
                                is com.walkyriasys.pyme.facturacion.domain.print.PrintResult.PrintFailed -> 
                                    "Print failed: ${result.reason}"
                                is com.walkyriasys.pyme.facturacion.domain.print.PrintResult.UnexpectedError -> 
                                    "Unexpected error: ${result.reason}"
                                else -> "Unknown error occurred"
                            }
                            _uiState.value = currentState.copy(
                                message = "Failed to print receipt: $errorMessage"
                            )
                        }
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
