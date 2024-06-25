
import com.android.build.gradle.internal.api.ApkVariantOutputImpl

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
//    alias(libs.plugins.kotlin.kapt)
    kotlin("kapt")

}

android {
    namespace = AppConfig.namespace
    compileSdk = AppConfig.compileSdk
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }


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
    implementation(libs.material)
    implementation(project(":lib_base"))
    implementation(libs.androidx.activity)
    if (!ModuleConfig.isApp) {
        implementation(project(":module_app_manager"))
        implementation(project(":module_constellation"))
        implementation(project(":module_joke"))
        implementation(project(":module_map"))
        implementation(project(":module_setting"))
        implementation(project(":module_weather"))
        implementation(project(":module_voice_setting"))
        implementation(project(":module_developer"))
    }
    //运行时注解
    kapt(libs.arouter.compiler)
}