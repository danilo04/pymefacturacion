package com.walkyriasys.pyme.facturacion.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayoneapp.dayone.di.IOThreadDispatcher
import com.walkyriasys.pyme.facturacion.domain.database.dao.OrderDao
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    @IOThreadDispatcher private val ioThreadDispatcher: CoroutineDispatcher,
    private val orderDao: OrderDao
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow<Order.OrderStatus?>(null)
    val selectedFilter: StateFlow<Order.OrderStatus?> = _selectedFilter.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadOrders()
    }

    fun setFilter(status: Order.OrderStatus?) {
        _selectedFilter.value = status
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val selectedStatus = _selectedFilter.value
                if (selectedStatus != null) {
                    orderDao.getOrdersByStatus(selectedStatus).collect { orders ->
                        _uiState.value = if (orders.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Loaded(
                                orders = orders,
                                selectedFilter = selectedStatus
                            ) {
                                // No load more for filtered results
                            }
                        }
                    }
                } else {
                    orderDao.getOrders(PAGE_SIZE, 0).collect { orders ->
                        _uiState.value = if (orders.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Loaded(
                                orders = orders,
                                selectedFilter = null
                            ) {
                                // TODO: Implement load more for all orders
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Empty // Handle error as empty for now
            }
        }
    }

    sealed interface UiState {
        data object Loading : UiState
        data object Empty : UiState
        data class Loaded(
            val orders: List<Order>,
            val selectedFilter: Order.OrderStatus?,
            val loadMore: () -> Unit
        ) : UiState
    }
    
    companion object {
        private const val PAGE_SIZE = 50
    }
}