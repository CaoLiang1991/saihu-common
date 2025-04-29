plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    kotlin("plugin.serialization") version "2.0.0"
    kotlin("kapt")
}

android {
    namespace = "com.saihu.common"
    compileSdk = 35

    defaultConfig {
        minSdk = 25

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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
}

dependencies {
    api("com.google.code.gson:gson:2.12.1")
    api("androidx.core:core-ktx:1.15.0")

    api("androidx.activity:activity-compose:1.10.1")
    api(platform("androidx.compose:compose-bom:2025.03.01"))
    val uiVersion = "1.7.8"
    api("androidx.compose.ui:ui:$uiVersion")
    api("androidx.compose.ui:ui-graphics:$uiVersion")
    api("androidx.compose.ui:ui-tooling-preview:$uiVersion")
    val material3Version = "1.3.1"
    api("androidx.compose.material3:material3:$material3Version")
    val pagingVersion = "3.3.6"
    api("androidx.paging:paging-runtime-ktx:$pagingVersion")
    api("androidx.paging:paging-compose:$pagingVersion")
    api("io.coil-kt:coil-compose:2.7.0")

    // Ktor client core
    val ktorVersion = "2.3.11"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    api("io.ktor:ktor-client-serialization:$ktorVersion")

    api(libs.apollo.runtime)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    api("io.reactivex.rxjava3:rxjava:3.1.8")

    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.2.1")
    androidTestApi("androidx.test.espresso:espresso-core:3.6.1")
}