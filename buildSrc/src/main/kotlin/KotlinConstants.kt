object KotlinConstants {
    const val TAG = "KotlinConstants"

}
object AppConfig{
    const val minSdk=26
    const val targetSdk=33
    const val compileSdk=34
    const val versionCode=1
    const val versionName="1.0"
    const val applicationId="com.example.aivoiceapplication"
    const val namespace= applicationId
    const val testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner"
    const val keyPassword="123456"
    const val keyAlias="key0"
    const val storePassword="123456"
    const val storeFileUrl="E:/Users/mlf/AndroidProjects/AiVoiceApplication/AiVoiceApplication/app/src/main/jks/aivoice.jks"
}
//Module配置
object ModuleConfig{
    //是否App
    var isApp=false
    //编译工具版本
    const val toolsVersion = "34.0.0"
    //包名
    const val MODULE_APP_MANAGER="com.example.module_app_manager"
    const val MODULE_CONSTELLATION="com.example.module_constellation"
    const val MODULE_DEVELOPER="com.example.module_developer"
    const val MODULE_JOKE="com.example.module_joke"
    const val MODULE_MAP="com.example.module_map"
    const val MODULE_SETTING="com.example.module_setting"
    const val MODULE_VOICE_SETTING="com.example.module_voice_setting"
    const val MODULE_WEATHER="com.example.module_weather"

}
