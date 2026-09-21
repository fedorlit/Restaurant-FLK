package com.example.restaurantflk.features.home.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.features.components.CartProductCard
import com.example.restaurantflk.features.components.InfoCard
import com.example.restaurantflk.features.components.LoadingCard
import com.example.restaurantflk.features.components.PrimaryButton
import com.example.restaurantflk.features.components.QuantityStepper
import com.example.restaurantflk.features.util.DisplayResult
import com.example.restaurantflk.ui.theme.BorderIdle
import com.example.restaurantflk.ui.theme.BrandBrown
import com.example.restaurantflk.ui.theme.BrandYellow
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.IconPrimary
import com.example.restaurantflk.ui.theme.Resources
import com.example.restaurantflk.ui.theme.Surface
import com.example.restaurantflk.ui.theme.SurfaceDarker
import com.example.restaurantflk.ui.theme.TextPrimary
import com.example.restaurantflk.ui.theme.TextWhite
import com.example.restaurantflk.ui.theme.sentientVariable
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreen(
    navigateToCheckout: (Double) -> Unit,
    navigateToMenu:() -> Unit
){
    val viewModel = koinViewModel<CartViewModel>()
    val  uiState by viewModel.uiState.collectAsState()

    uiState.cartItems.DisplayResult(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(),
        onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
        onError = { message ->
            InfoCard(
                image = Resources.Icon.Error,
                title = "Error",
                subtitle = message
            )
        },
        onSuccess = { cartItems ->
            val subTotal = viewModel.subTotal(cartItems)
            val total = viewModel.totalAmount(subTotal)

            Column(modifier = Modifier.fillMaxSize()){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cartItems, key = {it.product.id} ){ item ->
                        CartProductCard(
                            product = item.product,
                            onClick = {},
                            trailingContent = {
                                Row(
                                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    QuantityStepper(
                                        quantity = item.quantity,
                                        minValue = 1,
                                        onPlusClick = { viewModel.increment(item)},
                                        onMinusClick = { viewModel.decrement(item.product.id)}
                                    )
                                    IconButton(onClick = {viewModel.delete(item.product.id)}) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Delete),
                                            contentDescription = "Delete icon",
                                            tint = IconPrimary
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                CartSummaryCard(
                    promoCode = uiState.promoCode,
                    onPromoChanged = viewModel::onPromoCodeChanged,
                    onApplyPromo = {},
                    subTotal = subTotal,
                    deliveryFee = uiState.deliveryFee,
                    ivaPercent = uiState.ivaPercent,
                    totalAmount = total,
                    onCheckout = { navigateToCheckout(total) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                PrimaryButton(
                    text = "Ver más productos",
                    icon = painterResource(Resources.Icon.Book),
                    enabled = true,
                    onClick = navigateToMenu,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
private fun CartSummaryCard(
    promoCode: String,
    onPromoChanged:(String) -> Unit,
    onApplyPromo:() -> Unit,
    subTotal: Double,
    deliveryFee: Double,
    ivaPercent: Double,
    totalAmount: Double,
    onCheckout:() -> Unit
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(SurfaceDarker)
    ) {
        Column(modifier = Modifier.padding(12.dp)){
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                OutlinedTextField(
                    value = promoCode,
                    onValueChange = onPromoChanged,
                    placeholder = {
                        Text(
                            text = "Enter promo code",
                            fontFamily = sentientVariable(),
                            fontSize = FontSize.REGULAR
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandYellow,
                        unfocusedBorderColor = BorderIdle,
                        unfocusedContainerColor = Surface
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = onApplyPromo,
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(1.dp, BrandBrown),
                    modifier = Modifier.height(42.dp)
                ) {
                    Text(
                        text = "Aplicar",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            SummaryRow(label = "Subtotal", value = "${"%.2f".format(subTotal)}€")
            SummaryRow(label = "Gastos de envío", value = "${"%.2f".format(deliveryFee)}€")
            SummaryRow(label = "IVA %", value = "${(ivaPercent * 100 ).toInt()}%")

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = Color.Black.copy(0.8f)
            )

            SummaryRow(label = "TOTAL", value = "${"%.2f".format(totalAmount)}€", bold = true)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCheckout,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(BrandBrown),
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Checkout",
                    fontWeight = FontWeight.Bold,
                    fontSize = FontSize.REGULAR,
                    color = TextWhite
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    bold: Boolean = false
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = FontSize.REGULAR,
            color = TextPrimary,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = FontSize.REGULAR,
            color = TextPrimary,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
    }
}