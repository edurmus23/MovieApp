plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.movieapp.core.domain"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.retrofit.gson)
    implementation(libs.androidx.paging.runtime)
    
    // Firebase Firestore (Serialization desteği için)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
}