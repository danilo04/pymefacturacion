package com.walkyriasys.pyme.facturacion.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.walkyriasys.pyme.facturacion.domain.repositories.ProductsRepository
import com.walkyriasys.pyme.facturacion.navigation.Navigator
import com.walkyriasys.pyme.facturacion.ui.SnackbarManager
import com.walkyriasys.pyme.facturacion.ui.models.ProductItem
import com.walkyriasys.pyme.facturacion.ui.models.toProduct
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = AddEditProductViewModel.Factory::class)
class AddEditProductViewModel @AssistedInject constructor(
    private val productsRepository: ProductsRepository,
    @Assisted("navigator") private val navigator: Navigator,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("navigator") navigator: Navigator,
        ): AddEditProductViewModel
    }

    fun addProduct(productItem: ProductItem) {
        viewModelScope.launch {
            val product = productItem.toProduct()
            val productId = productsRepository.addProduct(product)
            when {
                (productId > 0) -> {
                    SnackbarManager.showMessage("Product added successfully.")
                    navigator.goBack()
                }

                else -> {
                    // TODO(Danilo): show error
                }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun genNewUuid(): String = Uuid.random().toString()
}