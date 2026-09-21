package com.example.restaurantflk.features.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.features.components.InfoCard
import com.example.restaurantflk.features.util.Alpha
import com.example.restaurantflk.ui.theme.BrandBrown
import com.example.restaurantflk.ui.theme.BrandYellow
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.IconPrimary
import com.example.restaurantflk.ui.theme.Resources
import com.example.restaurantflk.ui.theme.Surface
import com.example.restaurantflk.ui.theme.SurfaceBrand
import com.example.restaurantflk.ui.theme.SurfaceDarker
import com.example.restaurantflk.ui.theme.SurfaceLighter
import com.example.restaurantflk.ui.theme.TextPrimary
import com.example.restaurantflk.ui.theme.TextWhite
import com.example.restaurantflk.ui.theme.oswaldVariableFont

enum class PaymentMethod {
    Card,
    Paypal
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navigateBack: () -> Unit,
    totalAmount: Double
) {
    var method by remember { mutableStateOf(PaymentMethod.Card) }
    var savedCard by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        fontFamily = oswaldVariableFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                actions = {
                    Text(
                        text = "${"%.2f".format(totalAmount)}€",
                        fontSize = FontSize.MEDIUM,
                        color = BrandBrown,
                        fontWeight = FontWeight.Bold,
                        fontFamily = oswaldVariableFont(),
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentMethodToggle(
                selected = method,
                onSelect = { method = it }
            )

            // Contenedor con weight para empujar el botón al fondo sin alterar el diseño
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDarker)
                ) {
                    Column(modifier = Modifier.padding(12.dp)){
                        if(method == PaymentMethod.Card){
                            CardPaymentForm(
                                savedCard = savedCard,
                                onToggleSave = { savedCard = it }
                            )
                        }else{
                            PaypalPlaceHolder()
                        }
                    }
                }

                DeliveryDetailsCard(
                    address = "Calle Ejemplo 123",
                    postCode = "01234",
                    onEditAddress = {},
                    onEditPostCode = {}
                )
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(BrandYellow)
            ) {
                Text(
                    text = "Finalizar compra",
                    fontFamily = oswaldVariableFont(),
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun PaymentMethodToggle(
    selected: PaymentMethod,
    onSelect: (PaymentMethod) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        TogglePill(
            text = "Tarjeta",
            selected = selected == PaymentMethod.Card,
            onClick = { onSelect(PaymentMethod.Card) },
            leadingIcon = Resources.Icon.CreditCard
        )
        Spacer(modifier = Modifier.width(12.dp))
        TogglePill(
            text = "Paypal",
            selected = selected == PaymentMethod.Paypal,
            onClick = { onSelect(PaymentMethod.Paypal) },
            leadingIcon = Resources.Image.PaypalLogo
        )
    }
}

@Composable
private fun TogglePill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    leadingIcon: Int
) {
    val background = if (selected) BrandBrown else SurfaceLighter
    val foreground = if (selected) TextWhite else TextPrimary

    Button(
        onClick = onClick,
        modifier = Modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = background),
        border = BorderStroke(1.dp, BrandBrown)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = foreground
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(leadingIcon),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun CardPaymentForm(
    savedCard: Boolean,
    onToggleSave: (Boolean) -> Unit
) {
    Text(
        text = "Tarjeta de crédito",
        fontSize = FontSize.REGULAR,
        fontWeight = FontWeight.Bold,
        color = TextPrimary.copy(alpha = Alpha.HALF)
    )
    Spacer(modifier = Modifier.height(4.dp))
    OutlinedTextField(
        value = "1234 5678 9012 3456",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        trailingIcon = {
            Icon(
                painter = painterResource(Resources.Icon.CreditCard),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Nombre del titular",
        fontSize = FontSize.REGULAR,
        fontWeight = FontWeight.Bold,
        color = TextPrimary.copy(alpha = Alpha.HALF)
    )
    Spacer(modifier = Modifier.height(4.dp))
    OutlinedTextField(
        value = "Nombre y apellidos",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
    )

    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Fecha de caducidad",
        fontSize = FontSize.REGULAR,
        fontWeight = FontWeight.Bold,
        color = TextPrimary.copy(alpha = Alpha.HALF)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        DropdownStub(value = "01", modifier = Modifier.weight(1f))
        DropdownStub(value = "26", modifier = Modifier.weight(1f))
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "CVV",
        fontSize = FontSize.REGULAR,
        fontWeight = FontWeight.Bold,
        color = TextPrimary.copy(alpha = Alpha.HALF)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CvvBox(value = "-")
        CvvBox(value = "-")
        CvvBox(value = "-")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Guardar detalles de la tarjeta",
            fontSize = FontSize.REGULAR,
            color = TextPrimary
        )
        Switch(
            checked = savedCard,
            onCheckedChange = onToggleSave,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Surface,
                checkedTrackColor = SurfaceBrand,
                uncheckedTrackColor = SurfaceBrand,
                uncheckedThumbColor = Surface,
                checkedBorderColor = SurfaceBrand,
                uncheckedBorderColor = SurfaceDarker
            )
        )
    }
}

@Composable
private fun DropdownStub(
    value: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = modifier.width(120.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        trailingIcon = {
            Icon(
                painter = painterResource(Resources.Icon.Dropdown),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandYellow
        )
    )
}

@Composable
private fun CvvBox(value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        modifier = Modifier.width(54.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandYellow
        )
    )
}

@Composable
private fun DeliveryDetailsCard(
    address: String,
    postCode: String,
    onEditAddress: () -> Unit,
    onEditPostCode: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(
            modifier = Modifier
                .border(
                    1.dp,
                    BrandBrown,
                    RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = "Detalles de envío",
                fontSize = FontSize.REGULAR,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.alpha(0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            DeliveryRow(value = address, onEdit = onEditAddress)
            Spacer(modifier = Modifier.height(8.dp))
            DeliveryRow(value = postCode, onEdit = onEditPostCode)
        }
    }
}

@Composable
private fun DeliveryRow(
    value: String,
    onEdit: () -> Unit
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(SurfaceLighter)
            .padding(12.dp)
    ) {
        Text(
            text = value,
            fontSize = FontSize.REGULAR,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.width(12.dp))
        OutlinedButton(
            onClick = onEdit,
            modifier = Modifier.width(100.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(Surface)
        ){
            Text(
                text = "Editar",
                fontSize = FontSize.REGULAR,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun PaypalPlaceHolder(){
    Column(modifier = Modifier.height(200.dp)) {
        InfoCard(
            image = Resources.Icon.Error,
            title = "Error",
            subtitle = "Paypal no disponible en este momento"
        )
    }
}