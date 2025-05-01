package com.walkyriasys.pyme.facturacion.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.facturacion.domain.database.models.OrderItem
import com.walkyriasys.pyme.facturacion.domain.repositories.OrdersRepository
import com.walkyriasys.pyme.facturacion.navigation.Navigator
import com.walkyriasys.pyme.facturacion.ui.SnackbarManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = AddEditOrderViewModel.Factory::class)
class AddEditOrderViewModel @AssistedInject constructor(
    private val ordersRepository: OrdersRepository,
    @Assisted("navigator") private val navigator: Navigator,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("navigator") navigator: Navigator,
        ): AddEditOrderViewModel
    }

    fun addOrder(order: Order, orderItems: List<OrderItem>) {
        viewModelScope.launch {
            try {
                // First, save the order to get its ID
                val orderId = ordersRepository.addOrder(order)

                if (orderId > 0) {
                    // Then save the order items with the correct order ID
                    val items = orderItems.map { item ->
                        item.copy(orderId = orderId)
                    }

                    // Insert all order items
                    items.forEach { item ->
                        ordersRepository.addOrderItem(item)
                    }

                    SnackbarManager.showMessage("Order created successfully for ${order.customerName}.")
                    navigator.goBack()
                } else {
                    SnackbarManager.showMessage("Failed to create order.")
                }
            } catch (e: Exception) {
                SnackbarManager.showMessage("Error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun genNewUuid(): String = Uuid.random().toString()
}