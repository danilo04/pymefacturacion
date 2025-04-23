package com.walkyriasys.pyme.facturacion.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayoneapp.dayone.di.IOThreadDispatcher
import com.walkyriasys.pyme.facturacion.domain.database.dao.OrderDao
import com.walkyriasys.pyme.facturacion.domain.database.models.Order
import com.walkyriasys.pyme.utils.PagingDataSource
import com.walkyriasys.pyme.utils.debounceAndSend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    @IOThreadDispatcher private val ioThreadDispatcher: CoroutineDispatcher,
    private val orderDao: OrderDao
) : ViewModel() {

    private var loadMoreRef: (() -> Unit)? = null
    private val paginationSource: PagingDataSource<Unit, Order, UiState> = PagingDataSource(
        viewModelScope = viewModelScope,
        backgroundDispatcher = ioThreadDispatcher,
        loadPage = { _, page ->
            orderDao.getOrders(PAGE_SIZE, page * PAGE_SIZE)
        },
        builder = { pages ->
            pages.map { page ->
                val orders = page.data

                loadMoreRef = page.loadMore

                if (orders.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Loaded(
                        orders = orders
                    ) {
                        loadMoreRef?.invoke()
                    }
                }
            }
        }
    )

    @OptIn(FlowPreview::class)
    val uiState: Flow<UiState> = paginationSource.uiState.debounceAndSend(
        timeout = 200,
        emittedItemCount = 40,
        scope = viewModelScope
    )

    init {
        paginationSource.start(Unit)
    }

    sealed interface UiState {
        data object Loading : UiState
        data object Empty : UiState
        data class Loaded(
            val orders: List<Order>,
            val loadMore: () -> Unit
        ) : UiState
    }
    
    companion object {
        private const val PAGE_SIZE = 50
    }
}