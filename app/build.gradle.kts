plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.timer_java"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.timer_java"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Add this for Google Play Services (Location)
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.material:material:1.9.0") // Or latest version

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    dependencies {
    // Other dependencies...

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Converter for JSON parsing
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Gson for JSON serialization/deserialization
    implementation("com.google.code.gson:gson:2.8.9")

    // OkHttp for logging (optional but helpful for debugging)
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
}

}
