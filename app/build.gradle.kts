plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)

    id("com.google.devtools.ksp")

    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.restaurantflk"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.restaurantflk"

        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.compose.foundation)
    // =========================================================
    // ANDROID
    // =========================================================

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)


    // =========================================================
    // COMPOSE
    // =========================================================

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    debugImplementation(libs.androidx.compose.ui.tooling)


    // =========================================================
    // NAVIGATION
    // =========================================================

    implementation(libs.androidx.navigation.compose)


    // =========================================================
    // SERIALIZATION
    // =========================================================

    implementation(libs.kotlinx.serialization.json)


    // =========================================================
    // KOIN
    // =========================================================

    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.annotations)

    ksp(libs.koin.ksp.compiler)


    // =========================================================
    // ROOM
    // =========================================================

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)


    // =========================================================
    // DATASTORE
    // =========================================================

    implementation(libs.androidx.datastore.preferences)


    // =========================================================
    // COIL 3
    // =========================================================

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)


    // =========================================================
    // RETROFIT
    // =========================================================

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)


    // =========================================================
    // OKHTTP
    // =========================================================

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)


    // =========================================================
    // FIREBASE
    // =========================================================

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)


    // =========================================================
    // GOOGLE SIGN-IN / CREDENTIAL MANAGER
    // =========================================================

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)


    // =========================================================
    // COROUTINES
    // =========================================================

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)


    // =========================================================
    // TESTS
    // =========================================================

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )
}
