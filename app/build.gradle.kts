plugins {
    alias(libs.plugins.android.application)
}

android {

    buildFeatures {
        viewBinding=true
    }

    namespace = "edu.ucsd.cse110.habitizer.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "edu.ucsd.cse110.habitizer.app"
        minSdk = 34
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
}

dependencies {

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation(libs.android.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation(project(":lib"))
    implementation(project(":lib"))
    implementation(project(":observables"))
    implementation(project(":observables"))

    testImplementation(libs.junit4)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.espresso.core)
}

