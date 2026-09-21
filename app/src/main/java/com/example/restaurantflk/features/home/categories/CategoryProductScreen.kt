package com.example.restaurantflk.features.home.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.features.components.InfoCard
import com.example.restaurantflk.features.components.LoadingCard
import com.example.restaurantflk.features.components.ProductCard
import com.example.restaurantflk.features.util.DisplayResult
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.IconPrimary
import com.example.restaurantflk.ui.theme.Resources
import com.example.restaurantflk.ui.theme.Surface
import com.example.restaurantflk.ui.theme.TextPrimary
import com.example.restaurantflk.ui.theme.oswaldVariableFont
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductScreen(
    category: String,
    onNavigateBack: () -> Unit,
    onProductClick: (String) -> Unit
){
    val viewModel = koinViewModel<FoodMenuViewModel>()
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = category,
                        fontFamily = oswaldVariableFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back arrow icon",
                            tint = IconPrimary
                        )
                    }
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
        state.DisplayResult(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(12.dp),
            onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
            onError = { error ->
                InfoCard(
                    image = Resources.Icon.Error,
                    title = "Error",
                    subtitle = error
                )
            },
            onSuccess = { list ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    if(list.isEmpty()){
                        InfoCard(
                            image = Resources.Icon.Error,
                            title = "Error",
                            subtitle = "No hay productos en esta categoría"
                        )
                    }else{
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)){
                            items(list, key = {it.product.id}) { item ->
                                ProductCard(
                                    product = item.product,
                                    showFavouriteAction = true,
                                    isFavourite = item.isFavourite,
                                    onToggleFavourite = viewModel::toggleFavourite,
                                    onClick = onProductClick
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}