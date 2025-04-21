package com.walkyriasys.pyme.facturacion.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dayoneapp.dayone.di.IOThreadDispatcher
import com.walkyriasys.pyme.facturacion.domain.database.models.Product
import com.walkyriasys.pyme.facturacion.domain.repositories.ProductsRepository
import com.walkyriasys.pyme.facturacion.ui.models.ProductItem
import com.walkyriasys.pyme.facturacion.ui.models.toProductItem
import com.walkyriasys.pyme.utils.PagingDataSource
import com.walkyriasys.pyme.utils.debounceAndSend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    @IOThreadDispatcher private val ioThreadDispatcher: CoroutineDispatcher,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private var loadMoreRef: (() -> Unit)? = null
    private val paginationSource: PagingDataSource<Unit, Product, UiState> = PagingDataSource(
        viewModelScope = viewModelScope,
        backgroundDispatcher = ioThreadDispatcher,
        loadPage = { _, page ->
            productsRepository.getProducts(page = page)
        },
        builder = { pages ->
            pages.map { page ->
                val productItems = page.data.map { it.toProductItem() }

                loadMoreRef = page.loadMore

                UiState.Loaded(
                    products = productItems
                ) {
                    loadMoreRef?.invoke()
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
        data class Loaded(
            val products: List<ProductItem>,
            val loadMore: () -> Unit
        ) : UiState
    }
}