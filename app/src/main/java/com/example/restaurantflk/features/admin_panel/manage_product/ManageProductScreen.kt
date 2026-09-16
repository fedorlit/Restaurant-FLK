package com.example.restaurantflk.features.admin_panel.manage_product

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.restaurantflk.core.data.models.ProductCategory
import com.example.restaurantflk.features.components.BurgerSelectTextField
import com.example.restaurantflk.features.components.BurgerTextField
import com.example.restaurantflk.features.components.PrimaryButton
import com.example.restaurantflk.features.components.dialog.CategoryDialog
import com.example.restaurantflk.ui.theme.BorderIdle
import com.example.restaurantflk.ui.theme.FontSize
import com.example.restaurantflk.ui.theme.IconPrimary
import com.example.restaurantflk.ui.theme.Resources
import com.example.restaurantflk.ui.theme.Surface
import com.example.restaurantflk.ui.theme.SurfaceLighter
import com.example.restaurantflk.ui.theme.TextPrimary
import com.example.restaurantflk.ui.theme.oswaldVariableFont
import org.koin.androidx.compose.koinViewModel
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.restaurantflk.features.components.ErrorCard
import com.example.restaurantflk.features.components.LoadingCard
import com.example.restaurantflk.features.util.DisplayResult
import com.example.restaurantflk.features.util.MessageUtils
import com.example.restaurantflk.features.util.RequestState
import com.example.restaurantflk.ui.theme.ButtonPrimary
import com.example.restaurantflk.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id: String?,
    navigateBack: () -> Unit
) {
    val viewModel = koinViewModel<ManageProductViewModel>()
    val screenState = viewModel.screenState
    var showToast by remember { mutableStateOf("") }
    val isFormValid = viewModel.isFormValid
    val createProductState by viewModel.createProductState.collectAsState()

    MessageUtils.ShowToast(message = showToast)
    val context = LocalContext.current

    val productImageUploadState = viewModel.imageUploaderState

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri : Uri? ->
             viewModel.uploadProductImageToStorage(uri)
        }
    )
    LaunchedEffect(createProductState) {
        if (createProductState.isSuccess()) {
            showToast = "Producto creado exitosamente"
            viewModel.resetCreateProductState()
            navigateBack()
        }
        if(createProductState.isError()){
            showToast = createProductState.getErrorMessage()
        }
    }

    AnimatedVisibility(
        visible = screenState.isCategoryDialogOpen
    ) {
        CategoryDialog(
            categories = screenState.allCategories,
            onDismiss = viewModel::onCategoryDialogDismiss,
            onSelectedCategory = viewModel::onCategorySelected
        )
    }
    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == null) "Añadir Producto" else "Editar Producto",
                        fontFamily = oswaldVariableFont(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(paddingValues)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = BorderIdle,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(
                            enabled = productImageUploadState.isIdle()
                        ){
                            if(!productImageUploadState.isLoading()){
                                imagePickerLauncher.launch("image/*")
                            }
                        }
                        .background(SurfaceLighter),
                    contentAlignment = Alignment.Center
                ) {
                    productImageUploadState.DisplayResult(
                        onIdle = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(Resources.Icon.Plus),
                                contentDescription = "Add icon",
                                tint = IconPrimary
                            )
                        },
                        onLoading = {
                            LoadingCard(modifier = Modifier.fillMaxSize())
                        },
                        onSuccess = { _ ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                AsyncImage(
                                    model= ImageRequest.Builder(
                                        context
                                    ).data(screenState.productImage)
                                        .crossfade(enable = true)
                                        .build(),
                                    contentDescription = "Product image",
                                    modifier = Modifier.matchParentSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            top = 12.dp,
                                            end = 12.dp
                                        )
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ButtonPrimary)
                                        .clickable{
                                            viewModel.deleteProductImageFromStorage{ isSuccess, message ->
                                                showToast = message
                                            }
                                        }
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ){
                                    Icon(
                                        modifier = Modifier.size(24.dp),
                                        painter = painterResource(Resources.Icon.Delete),
                                        contentDescription = "Delete icon",
                                        tint = IconPrimary
                                    )
                                }
                            }
                        },
                        onError = { message: String ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                ErrorCard(message = message)
                                Spacer(modifier = Modifier.height(12.dp))

                                TextButton(
                                    onClick = {
                                        viewModel.updateImageState(RequestState.Idle)
                                    }
                                ) {
                                    Text(
                                        text = "Intentar de nuevo",
                                        fontSize = FontSize.SMALL,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    )
                }

                BurgerTextField(
                    value = screenState.title,
                    onValueChange = viewModel::updateTitle,
                    placeholder = "Título"
                )
                BurgerTextField(
                    modifier = Modifier.height(120.dp),
                    value = screenState.description,
                    onValueChange = viewModel::updateDescription,
                    placeholder = "Descripción",
                    expanded = true
                )
                BurgerSelectTextField(
                    modifier = Modifier.fillMaxWidth(),
                    text = screenState.selectedCategory?.title ?: "",
                    onClick = viewModel::onCategoryFieldClick,
                    placeholder = "Seleccionar Categoría"
                )
                BurgerTextField(
                    value = "${screenState.energyValue ?: ""}",
                    onValueChange = {viewModel.updateEnergyValue(it.toIntOrNull() ?: 0)},
                    placeholder = "Valor energético",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                BurgerTextField(
                    modifier = Modifier.height(80.dp),
                    value = screenState.allergyAdvice,
                    onValueChange = viewModel::updateAllergyAdvice,
                    placeholder = "Alérgenos",
                    expanded = true
                )
                BurgerTextField(
                    modifier = Modifier.height(80.dp),
                    value = screenState.ingredients,
                    onValueChange = viewModel::updateIngredients,
                    expanded = true,
                    placeholder = "Ingredientes"
                )
                BurgerTextField(
                    value = if (screenState.price == 0.0) ""
                            else "${screenState.price}",
                    onValueChange = {value ->
                        if(value.isEmpty() || value.toDoubleOrNull() != null){
                            viewModel.updatePrice(value.toDoubleOrNull() ?: 0.0)
                        }
                    },
                    placeholder = "Precio",
                    expanded = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = if (id == null) "Añadir producto" else "Actualizar producto",
                icon = if(id == null) painterResource(Resources.Icon.Plus)
                else painterResource(Resources.Icon.Checkmark),
                enabled = isFormValid && !createProductState.isLoading(),
                onClick = {
                    viewModel.createNewProduct()
                }
            )
        }
    }
}