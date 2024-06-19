
import com.android.build.gradle.internal.api.ApkVariantOutputImpl

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = AppConfig.namespace
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.testInstrumentationRunner
    }
    //签名类型
    signingConfigs{
        register("release"){
            //路径
            storeFile = file(AppConfig.storeFileUrl)
            //密码
            storePassword = AppConfig.storePassword
            //别名
            keyAlias = AppConfig.keyAlias
            //别名密码
            keyPassword = AppConfig.keyPassword

        }
    }


    //编译类型
    buildTypes {
        getByName("debug"){

        }
        getByName("release"){
            isMinifyEnabled = false
            //自动打包
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    //输出类型
    android.applicationVariants.all{
        //编译类型
        val buildType = this.buildType.name
        //作者名称
        val userName="MaLongFei666"
        outputs.all {
            if (this is ApkVariantOutputImpl){
                this.outputFileName="AI_V${defaultConfig.versionName}_${buildType}_$userName.apk"
            }
        }
    }



    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}