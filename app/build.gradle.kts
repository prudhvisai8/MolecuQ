import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt") // Required for Retrofit's Moshi converter if used
}

android {
    namespace = "com.quantum.molecuq"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.quantum.molecuq"
        minSdk = 29
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true // Enable code shrinking for release builds
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // You can add specific configurations for debug builds if needed
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
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx) // Use the latest version
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.constraint.layout)
    implementation(libs.androidx.annotation)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)

    implementation(libs.coil)

    implementation(libs.kotlinx.coroutines.android)
    // Retrofit dependencies
    implementation(libs.retrofit.core)
    implementation(libs.mpandroidchart)
    implementation(libs.retrofit.converter.gson) // Or another converter like Moshi
    implementation(libs.okhttp3.logging.interceptor) // For logging network requests (optional)

    implementation("com.opencsv:opencsv:5.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.cardview)
    // Optional: If using Moshi for JSON parsing with Retrofit
    //implementation(libs.retrofit.converter.moshi)
    // ksp(libs.moshi.kotlin.codegen) // For Moshi code generation (if using KSP)
    // kapt(libs.moshi.kotlin.codegen) // For Moshi code generation (if using kapt)
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
