package com.example.restaurantflk.features.home.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantflk.core.data.domain.CartRepository
import com.example.restaurantflk.core.data.models.Cart
import com.example.restaurantflk.core.data.models.CartItemUi
import com.example.restaurantflk.features.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val cartItems: RequestState<List<CartItemUi>> = RequestState.Loading,
    val promoCode: String = "",
    val deliveryFee: Double = 4.0,
    val ivaPercent: Double = 0.21,
)
class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState

    init {
        viewModelScope.launch {
            cartRepository.observeCartItems().collect{ start ->
                _uiState.update { it.copy(cartItems = start) }
            }
        }
    }

    fun onPromoCodeChanged(value: String){
        _uiState.update { it.copy(promoCode = value) }
    }

    fun increment(cartItem: CartItemUi) = viewModelScope.launch{
        cartRepository.increment(productId = cartItem.product.id, productTitle = cartItem.product.title)
    }

    fun decrement(productId: String) = viewModelScope.launch{
        cartRepository.decrement(productId)
    }

    fun delete(productId: String) = viewModelScope.launch{
        cartRepository.delete(productId)
    }

    fun subTotal(items: List<CartItemUi>): Double =
        items.sumOf { it.product.price * it.quantity }

    fun ivaAmount(subtTotal: Double): Double = subtTotal * _uiState.value.ivaPercent

    fun totalAmount(subTotal: Double): Double = subTotal + _uiState.value.deliveryFee + ivaAmount(subTotal)
}