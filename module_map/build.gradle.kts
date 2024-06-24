plugins {
    if (ModuleConfig.isApp){
        alias(libs.plugins.android.application)
    }else{
        alias(libs.plugins.android.library)
    }
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.kapt)

}

android {
    namespace = ModuleConfig.MODULE_MAP
    compileSdk = AppConfig.compileSdk
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
//        if (ModuleConfig.isApp) {applicationId = ModuleConfig.MODULE_MAP}
        minSdk = 26

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
//        consumerProguardFiles("consumer-rules.pro")
    }
    //动态替换资源
    sourceSets{
        getByName("main"){
            if (ModuleConfig.isApp){
                manifest.srcFile("src/main/manifest/AndroidManifest.xml")
            }else{
                manifest.srcFile("src/main/AndroidManifest.xml")

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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":lib_base"))
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
    //运行时注解
    kapt(libs.arouter.compiler)
}