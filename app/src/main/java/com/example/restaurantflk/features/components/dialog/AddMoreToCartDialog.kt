package com.example.restaurantflk.features.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.core.data.models.Product
import com.example.restaurantflk.features.components.CartProductCard
import com.example.restaurantflk.features.components.InfoCard
import com.example.restaurantflk.features.components.LoadingCard
import com.example.restaurantflk.features.util.Alpha
import com.example.restaurantflk.features.util.DisplayResult
import com.example.restaurantflk.features.util.RequestState
import com.example.restaurantflk.ui.theme.BrandYellow
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.IconPrimary
import com.example.restaurantflk.ui.theme.Resources
import com.example.restaurantflk.ui.theme.Surface
import com.example.restaurantflk.ui.theme.TextPrimary
import com.example.restaurantflk.ui.theme.oswaldVariableFont

@Composable
fun AddMoreToCartDialog(
    suggestedProducts: RequestState<List<Product>>,
    addedIds: Set<String>,
    totalPrice: Double,
    onDismiss: () -> Unit,
    onProductClick: (String) -> Unit,
    onAddChecked: (Product) -> Unit,
    onRemoveChecked: (Product) -> Unit,
    onCheckout: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = {
            Text(
                text = "¿ Algo más ?",
                fontFamily = oswaldVariableFont(),
                fontSize = FontSize.MEDIUM,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        },
        text = {
            suggestedProducts.DisplayResult(
                onIdle = {
                    Text(
                        modifier = Modifier.alpha(Alpha.HALF),
                        text = "Sin sugerencias",
                        fontFamily = oswaldVariableFont(),
                        fontSize = FontSize.REGULAR,
                        color = TextPrimary
                    )
                },
                onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
                onError = { message ->
                    InfoCard(
                        image = Resources.Icon.Error,
                        title = "Error",
                        subtitle = message
                    )
                },
                onSuccess = { products ->
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        products.take(10).forEach { product ->
                            val checked = addedIds.contains(product.id)

                            Box(modifier = Modifier.fillMaxWidth()){
                                CartProductCard(
                                    product = product,
                                    onClick = onProductClick
                                )
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { nowChecked ->
                                        when{
                                            nowChecked && !checked ->{
                                                onAddChecked(product)
                                            }
                                            !nowChecked && checked -> {
                                                onRemoveChecked(product)
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(end = 6.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }
                        }
                    }
                }
            )
        },
        confirmButton = {
            Button(
                onClick = onCheckout,
                modifier = Modifier.height(44.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandYellow)
            ){
                Text(
                    text = "Checkout (${"%.2f".format(totalPrice)}€)",
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(Resources.Icon.ShoppingCart),
                    contentDescription = "Shopping cart icon",
                    modifier = Modifier.size(18.dp),
                    tint = IconPrimary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.height(44.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Close),
                    contentDescription = "Close icon",
                    modifier = Modifier.size(16.dp),
                    tint = IconPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Cerrar",
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    fontFamily = oswaldVariableFont()
                )
            }
        }
    )
}