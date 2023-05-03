@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.objectorientedoleg.domain"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(project(":core:colorpalette"))
    implementation(project(":core:data"))

    implementation(libs.androidx.paging.runtime)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.android)

    kapt(libs.hilt.compiler)

    androidTestImplementation(libs.androidx.test.ext)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}