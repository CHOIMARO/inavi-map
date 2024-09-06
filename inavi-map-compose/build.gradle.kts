plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.inavi_map_compose"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    buildFeatures {
        buildConfig = false
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=warning"
    }
}

dependencies {
    api(libs.inavi.maps.sdk)

    implementation(libs.compose.foundation)
    implementation(libs.kotlin.stdlib)

    // To provide FusedLocationSource
    implementation(libs.play.services.location)
    implementation(libs.accompanist.permissions)
    implementation(project(":inavi-map-location"))

}