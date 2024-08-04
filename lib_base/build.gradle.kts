
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
    defaultConfig {
        namespace = "com.example.lib_base"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        minSdk = AppConfig.minSdk
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
    api(libs.mpandroidchart)
    //lottie
    api(libs.lottie)
    //viewpage
    api(libs.androidx.viewpager2)
    api(project(":lib_network"))
    api(project(":lib_voice"))
    api(libs.permission.v200)
    api(libs.recyclerview)
    //eventbus
    api(libs.events)
    //arouter
    api(libs.arouter)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.activity)
    //运行时注解
    kapt(libs.arouter.compiler)
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.androidx.constraintlayout)
    api(libs.material)
    api(libs.autosize)
    api(libs.refresh.layout.kernel)
    api(libs.refresh.footer.ball)
    api(libs.refresh.header.material)
    api(libs.refresh.header.classics)
    api(libs.refresh.footer.classics)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}