plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.kerolsmm.appmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kerolsmm.appmanager"
        minSdk = 26
        targetSdk = 34
        versionCode = 8
        versionName = "1.0.8"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
        viewBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation ("com.github.skydoves:progressview:1.1.3")


    implementation ("com.google.code.gson:gson:2.10.1")

    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation ("com.github.bumptech.glide:glide:4.16.0")


    // 1. Use Guava in your implementation only:
    implementation("com.google.guava:guava:32.1.3-jre")

    // 2. Use Guava types in your public API:
    api("com.google.guava:guava:32.1.3-jre")

    // 3. Android - Use Guava in your implementation only:
    implementation("com.google.guava:guava:32.1.3-jre")

    // 4. Android - Use Guava types in your public API:
    api("com.google.guava:guava:32.1.3-jre")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


    implementation ("androidx.core:core-splashscreen:1.0.1")
    val multidex_version = "2.0.1"
    implementation("androidx.multidex:multidex:$multidex_version")

    implementation ("com.google.android.play:review-ktx:2.0.1")


    val fragment_version = "1.6.2"
    implementation ("androidx.fragment:fragment-ktx:1.8.1")

    implementation ("androidx.activity:activity-ktx:1.9.0")


}