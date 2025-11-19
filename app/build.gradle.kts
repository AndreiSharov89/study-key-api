import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

android {
    namespace = "com.example.key_api"
    compileSdk = 35
    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.example.key_api"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
/*            val imdbApiKey = providers.gradleProperty("API_KEY").getOrElse("")
            buildConfigField(
                "String",
                "IMDB_API_KEY",
                "\"$imdbApiKey\""
            )*/
            buildConfigField(
                "String",
                "IMDB_API_KEY",
                "\"${localProperties.getProperty("API_KEY")}\""
            )
        }
        /*release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val imdbApiKey = providers.gradleProperty("API_KEY").getOrElse("")
            buildConfigField(
                "String",
                "IMDB_API_KEY",
                "\"$imdbApiKey\""
            )
        }*/
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.retrofit)
    implementation(libs.glide)
    implementation(libs.koin.android)
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation(libs.koin.android)
    val fragment_version = "1.5.5"
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}