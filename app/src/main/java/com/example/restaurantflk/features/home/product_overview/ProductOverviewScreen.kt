package com.example.restaurantflk.features.home.product_overview

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.core.data.models.ProductCategory
import com.example.restaurantflk.features.components.CategoryChip
import com.example.restaurantflk.features.components.InfoCard
import com.example.restaurantflk.features.components.LoadingCard
import com.example.restaurantflk.features.components.MainProductCard
import com.example.restaurantflk.features.components.ProductCard
import com.example.restaurantflk.features.util.Alpha
import com.example.restaurantflk.features.util.DisplayResult
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.Resources
import com.example.restaurantflk.ui.theme.TextPrimary
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun ProductOverviewScreen(
    onProductClick: (String) -> Unit
){
    val viewModel = koinViewModel<ProductOverviewViewModel>()

    val heroProduct by viewModel.heroProduct.collectAsState()
    val heroPaused by viewModel.heroPaused.collectAsState()

    val popularProducts by viewModel.popularProducts.collectAsState()
    val discountedProducts by viewModel.discountedProducts.collectAsState()
    val categoryProducts by viewModel.categoryProduct.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val favouriteIdsState by viewModel.favouriteIds.collectAsState()
    val favouriteIds = favouriteIdsState.getSuccessDataOrNull().orEmpty()

    BackHandler(enabled = selectedCategory!=null){
        viewModel.clearCategory()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //MainProductCard
        item {
            AnimatedContent(
                targetState = heroProduct?.id,
                transitionSpec = {
                    fadeIn(tween(500)) togetherWith fadeOut(tween(500))
                }
            ) { _ ->
                heroProduct?.let { product ->
                    MainProductCard(
                        title = product.title,
                        energyValue = "${product.energyValue ?: 0} kcal",
                        price = "${"%.2f".format(product.price)}€",
                        imageUrl = product.productImage,
                        paused =heroPaused,
                        onClick = { onProductClick(product.id) }
                    )
                }?: LoadingCard(modifier = Modifier.fillMaxSize())
            }
        }

        // Category row

        item { SectionHeader(title = "Nuestro Menú")
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(ProductCategory.entries){category ->
                    CategoryChip(
                        title = category.title,
                        iconRes = category.icon,
                        onClick = { viewModel.selectedCategory(category) }
                    )
                }
            }
        }

        if(selectedCategory!=null){
            item { SectionHeader(title = selectedCategory!!.title) }
            item {
                categoryProducts.DisplayResult(
                    onLoading = { LoadingCard(modifier = Modifier.fillMaxSize())},
                    onError = { message ->
                        InfoCard(
                            image = Resources.Icon.Error,
                            title = "Error",
                            subtitle = message
                        )
                    },
                    onSuccess = {list ->
                        val products = list
                            .distinctBy { it.id }
                            .sortedByDescending { it.createdAt }
                        if(products.isEmpty()){
                            InfoCard(
                                image = Resources.Icon.Error,
                                title = "Error",
                                subtitle = "No hay productos en esta categoría"
                            )
                        }else{
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                products.forEach { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = onProductClick
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }else{
            item { SectionHeader(title = "Productos populares") }
            item {
                popularProducts.DisplayResult(
                    onLoading = { LoadingCard(modifier = Modifier.fillMaxSize())},
                    onError = { message ->
                        InfoCard(
                            image = Resources.Icon.Error,
                            title = "Error",
                            subtitle = message
                        )
                    },
                    onSuccess = {list ->
                        val products = list
                            .distinctBy { it.id }
                            .sortedByDescending { it.createdAt }
                        if(products.isEmpty()){
                            InfoCard(
                                image = Resources.Icon.Error,
                                title = "Error",
                                subtitle = "No hay productos populares"
                            )
                        }else{
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                products.forEach { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = onProductClick,
                                        showFavouriteAction = true,
                                        isFavourite = favouriteIds.contains(product.id),
                                        onToggleFavourite = viewModel::toggleFavourite
                                    )
                                }
                            }
                        }
                    }
                )
            }
            item { SectionHeader(title = "Productos con descuento") }
            item {
                discountedProducts.DisplayResult(
                    onLoading = { LoadingCard(modifier = Modifier.fillMaxSize())},
                    onError = { message ->
                        InfoCard(
                            image = Resources.Icon.Error,
                            title = "Error",
                            subtitle = message
                        )
                    },
                    onSuccess = {list ->
                        val products = list
                            .distinctBy { it.id }
                            .sortedByDescending { it.createdAt }
                        if(products.isEmpty()){
                            InfoCard(
                                image = Resources.Icon.Error,
                                title = "Error",
                                subtitle = "No hay productos con descuento"
                            )
                        }else{
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                products.forEach { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = onProductClick,
                                        showFavouriteAction = true,
                                        isFavourite = favouriteIds.contains(product.id),
                                        onToggleFavourite = viewModel::toggleFavourite
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String){
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(Alpha.HALF),
        text = title,
        fontSize = FontSize.EXTRA_REGULAR,
        color = TextPrimary,
        textAlign = TextAlign.Center
    )
}