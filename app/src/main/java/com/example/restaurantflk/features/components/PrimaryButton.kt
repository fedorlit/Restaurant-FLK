package com.example.restaurantflk.features.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.ui.theme.ButtonDisabled
import com.example.restaurantflk.ui.theme.ButtonPrimary
import com.example.restaurantflk.ui.theme.ButtonSecondary
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.TextPrimary

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    enabled: Boolean = true,
    secondary: Boolean = false,
    onClick: () -> Unit
){
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (secondary) ButtonSecondary else ButtonPrimary,
            contentColor = TextPrimary,
            disabledContainerColor = ButtonDisabled,
            disabledContentColor = TextPrimary.copy(0.6f)
        ),
        contentPadding = PaddingValues(20.dp)
    ){
        Text(
            text = text,
            fontSize = FontSize.MEDIUM,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (icon != null) {
            Icon(
                modifier=Modifier.size(20.dp),
                painter = icon,
                contentDescription = "Button Icon"
            )
        }
    }
}