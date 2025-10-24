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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    @IOThreadDispatcher private val ioThreadDispatcher: CoroutineDispatcher,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var loadMoreRef: (() -> Unit)? = null

    private val paginationSource: PagingDataSource<String, Product, UiState> = PagingDataSource(
        viewModelScope = viewModelScope,
        backgroundDispatcher = ioThreadDispatcher,
        loadPage = { searchQuery, page ->
            if (searchQuery.isBlank()) {
                productsRepository.getProducts(page = page)
            } else {
                productsRepository.searchProducts(searchQuery = searchQuery, page = page)
            }
        },
        builder = { pages ->
            pages.map { page ->
                val productItems = page.data.map { it.toProductItem() }

                loadMoreRef = page.loadMore

                if (productItems.isEmpty()) {
                    UiState.Empty
                } else {
                    UiState.Loaded(
                        products = productItems
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
        emittedItemCount = 20,
        scope = viewModelScope
    )

    init {
        paginationSource.start("")
        
        // Listen to search query changes and restart pagination
        viewModelScope.launch {
            _searchQuery.collect { query ->
                paginationSource.start(query)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    sealed interface UiState {
        data object Loading : UiState
        data object Empty : UiState
        data class Loaded(
            val products: List<ProductItem>,
            val loadMore: () -> Unit
        ) : UiState
    }
}