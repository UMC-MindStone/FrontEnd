plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.mindstone"
    compileSdk = 35

    buildFeatures {
        viewBinding {
            enable = true
        }
    }

    defaultConfig {
        applicationId = "com.example.mindstone"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material.calendarview)
    implementation(libs.circleindicator)
    implementation(libs.mpandroidchart)
    implementation(libs.androidx.ui.test.android)

    implementation("com.github.kizitonwose:CalendarView:1.0.4") // CalendarView
    implementation("me.relex:circleindicator:2.1.6") // CircleIndicator
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0") // MPAndroidChart

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ✅ OkHttp 라이브러리 추가
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3") // 로그 인터셉터 추가

    // ✅ Gson 추가
    implementation("com.google.code.gson:gson:2.8.9")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp 네트워크 요청
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")// 최신 버전 확인 가능
    
    // ✅ OkHttp Logging Interceptor (네트워크 요청 로그 확인)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
}
