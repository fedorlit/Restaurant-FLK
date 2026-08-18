package com.example.restaurantflk.core.di

import com.example.restaurantflk.R
import com.example.restaurantflk.core.data.auth.GoogleUIClient
import com.example.restaurantflk.core.data.domain.CustomerRepository
import com.example.restaurantflk.core.data.repoimpl.CustomerRepoImpl
import com.example.restaurantflk.features.auth.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<FirebaseAuth>{ FirebaseAuth.getInstance() }

    single<CustomerRepository>{ CustomerRepoImpl() }

    viewModel { AuthViewModel(get(),auth = get()) }

    single{
        GoogleUIClient(
            context = androidContext(),
            auth = get(),
            serverClient = androidContext().getString(R.string.default_web_client_id)
        )
    }
}