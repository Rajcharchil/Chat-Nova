plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.charchil.chatnova"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.charchil.chatnova"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.ai.client.generativeai:generativeai:0.2.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.airbnb.android:lottie:6.1.0")

    // Firebase Authentication (latest version)
    implementation ("com.google.firebase:firebase-auth-ktx:22.3.0")

    // Google Sign-In SDK (latest version)
    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.facebook.android:facebook-android-sdk:latest.release")
    // Firebase Storage

    implementation ("com.google.firebase:firebase-firestore-ktx:24.10.1")

    implementation ("com.google.mlkit:image-labeling:17.0.5")



//    // Import the BoM for the Firebase platform
//    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
//
//    // Add the dependency for the Firebase Authentication library
//    // When using the BoM, you don't specify versions in Firebase library dependencies
//    implementation("com.google.firebase:firebase-auth")
//
//    // Google Play Services
//    implementation("com.google.android.gms:play-services-auth:20.7.0")
//    implementation("androidx.credentials:credentials:1.1.0-alpha01")







}