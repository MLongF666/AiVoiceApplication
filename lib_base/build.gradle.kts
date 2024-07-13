import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)

}

android {
    namespace = "com.example.lib_base"
    compileSdk = 34
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
//    dataBinding{
//        enabled = true
//    }
    defaultConfig {
        minSdk = 26
        consumerProguardFiles("consumer-rules.pro")
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
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
}

dependencies {
    api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //lottie
    api(libs.lottie)
    //viewpage
    api("androidx.viewpager2:viewpager2:1.1.0")
    api(project(":lib_network"))
    api(project(":lib_voice"))
    //andpermission
//    api(libs.andpermission)
    api("com.yanzhenjie:permission:2.0.0")
    api(libs.recyclerview)
    //eventbus
    api(libs.events)
    //arouter
    api(libs.arouter)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    //运行时注解
    kapt(libs.arouter.compiler)
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.constraintlayout)
    api(libs.material)
//    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}