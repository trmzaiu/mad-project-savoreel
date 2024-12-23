plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.savoreel"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.savoreel"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.compose.ui:ui-text:1.7.5")
    implementation("androidx.compose.animation:animation:1.7.5")
    implementation("androidx.navigation:navigation-compose:2.7.1")
    implementation("com.google.accompanist:accompanist-flowlayout:0.24.13-rc")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.24.13-rc")
    implementation(libs.androidx.navigation.compose)
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")
    implementation(libs.androidx.storage)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.functions.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.runtime.livedata)

    // Correct dependencies for UI testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.5")
    androidTestImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.camera:camera-core:1.4.1")
    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")

    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:maps-compose:4.4.2")
    implementation("com.google.maps.android:maps-compose-widgets:4.3.2")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.maps.android:maps-compose-utils:4.4.2")
    implementation("com.google.android.libraries.places:places:3.3.0")

    implementation ("androidx.compose.ui:ui:1.7.6" )
    implementation ("androidx.compose.foundation:foundation:1.7.6")
    implementation ("androidx.compose.runtime:runtime:1.7.6")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.7.6") // This is required for @Preview to work

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
}