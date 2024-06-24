# 1、本项目核心思想

Kotlin+组件化+kotlin Gradle DSL

## 全局编译配置 

include/插入module编译项

rootProject.name/项目名称

rootProject.bulidFileName/指定Gradle编译配置文件

## 组件化架构思想

![image-20240619132910054](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202406191329233.png)

组件=App 可以是libral也可以是application

### 组件化概括

App空壳+若干组件

### 组建的构建和管理
Module
* 笑话
* 地图
* 星座
* 语音设置
* 系统设置
* 天气
* 应用管理
* 开发者模式

### ARouter

对ARouter进行了封装和测试

添加依赖

借鉴ARouter官网

https://github.com/alibaba/ARouter/blob/master/README_CN.md

封装的helper类

```kotlin
object ARouterHelper {
    const val PATH_APP_MANAGER="/app_manager/app_manager_activity"
    const val PATH_CONSTELLATION="/constellation/constellation_activity"
    const val PATH_DEVELOPER="/developer/developer_activity"
    const val PATH_JOKE="/joke/joke_activity"
    const val PATH_MAP="/map/map_activity"
    const val PATH_SETTING="/setting/setting_activity"
    const val PATH_VOICE_SETTING="/voice_setting/voice_setting_activity"
    const val PATH_WEATHER="/weather/weather_activity"
    //初始化
    fun initHelper(application:Application){
        //是否为debug 
        if(BuildConfig.DEBUG){
            //是的话 ARoute的path路径能够加载 否则无法加载
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)
    }
    //跳转页面
    fun startActivity(path:String){
        ARouter.getInstance().build(path).navigation()
    }
    //跳转页面
    fun startActivity(activity: Activity,path:String,requestCode:Int){
        ARouter.getInstance().build(path).navigation(activity,requestCode)
    }
    //跳转页面 传递一个string
    fun startActivity(path: String, key:String, value:String){
        ARouter.getInstance().build(path).withString(key,value).navigation()
    }
    // 跳转页面 传递一个int
    fun startActivity(path: String, key:String, value:Int){
        ARouter.getInstance().build(path).withInt(key,value).navigation()
    }
    //跳转页面 传递一个boole
    fun startActivity(path: String, key:String, value:Boolean){
        ARouter.getInstance().build(path).withBoolean(key,value).navigation()
    }
    //跳转页面 传递一个Long
    fun startActivity(path: String, key:String, value:Long){
        ARouter.getInstance().build(path).withLong(key,value).navigation()
    }
    //跳转页面 传递一个Bundle
    fun startActivity(path: String, key:String, value: Bundle){
        ARouter.getInstance().build(path).withBundle(key,value).navigation()
    }
    //跳转页面 传递一个对象
    fun startActivity(path: String, key:String, value: Any){
        ARouter.getInstance().build(path).withObject(key,value).navigation()
    }

}
```



### Koltin的封装

* BaseActivity/BaseFragment
* 
* Log日志
* Sharepreferences

