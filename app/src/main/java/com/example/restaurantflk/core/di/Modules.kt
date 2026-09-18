package com.example.restaurantflk.core.di

import com.example.restaurantflk.R
import com.example.restaurantflk.core.data.auth.GoogleUIClient
import com.example.restaurantflk.core.data.domain.AdminRepository
import com.example.restaurantflk.core.data.domain.CountryRepository
import com.example.restaurantflk.core.data.domain.CountryRepositoryImpl
import com.example.restaurantflk.core.data.domain.CustomerRepository
import com.example.restaurantflk.core.data.domain.ProductRepository
import com.example.restaurantflk.core.data.remote.RestCountriesApi
import com.example.restaurantflk.core.data.repoimpl.AdminRepoImpl
import com.example.restaurantflk.core.data.repoimpl.CustomerRepoImpl
import com.example.restaurantflk.core.data.repoimpl.ProductRepoImpl
import com.example.restaurantflk.features.admin_panel.AdminPanelViewModel
import com.example.restaurantflk.features.admin_panel.manage_product.ManageProductViewModel
import com.example.restaurantflk.features.auth.AuthViewModel
import com.example.restaurantflk.features.home.HomeViewModel
import com.example.restaurantflk.features.home.product_overview.ProductOverviewViewModel
import com.example.restaurantflk.features.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        Retrofit.Builder()
            .baseUrl("https://api.restcountries.com/")
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single<RestCountriesApi> { get<Retrofit>().create(RestCountriesApi::class.java) }
    single<CountryRepository>{ CountryRepositoryImpl(get()) }

    single<FirebaseAuth>{ FirebaseAuth.getInstance() }

    single<CustomerRepository>{ CustomerRepoImpl() }
    single<AdminRepository>{ AdminRepoImpl() }
    single<ProductRepository>{ ProductRepoImpl() }

    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ManageProductViewModel(get(), get())}
    viewModel { AdminPanelViewModel(get()) }
    viewModel{ ProductOverviewViewModel(get()) }

    single{
        GoogleUIClient(
            context = androidContext(),
            auth = get(),
            serverClient = androidContext().getString(R.string.default_web_client_id)
        )
    }
}
