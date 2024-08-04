## 本项目核心思想

Kotlin+组件化+kotlin Gradle DSL

## 全局编译配置 

include/插入module编译项

rootProject.name/项目名称

rootProject.bulidFileName/指定Gradle编译配置文件

## 组件化架构思想

![AI语音助手客户端架构图](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202406261058116.png)

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
* Log日志
* Sharepreferences

### AI

* TTS/发音
* ASR/语义识别
* WakeUp唤醒

### IntentService服务

* 使用场景/短任务
* 源码分析/ServiceHandler
* 生命周期/onHandleIntent

### Service常规保活手段

![image-20240625101531477](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202406251015618.png)

1. 像素保活通过服务中启动一个窗口像素1px，来达到保活手段[欺诈系统]
2. 系统自带，系统做了一些友好的保活  FLAG
   1. START_STICKY 保证服务一直运行，直到被系统杀死
   2. START_NOT_STICKY 默认值，服务被系统杀死，不会重启
   3. START_REDELIVER_INTENT 重启服务，但会保留最后一次的Intent
   4. STOP_FOREGROUND_SERVICE 停止前台服务
   5. START_STICKY_COMPATIBILITY 兼容模式，保证服务一直运行，直到被系统杀死，但不保证服务被杀死时能及时停止
3. JobSheduler
   1. 工作任务，标志着这个服务一直在工作，也是作为一种进程死后复活手段
   2. 缺点：耗电，高版本不兼容
4. 进程相互唤醒，双进程保活
   1. qq 微信
5. 前台服务
   1. 前台运行绑定通知栏，在服务中创建通知栏  

## 应用启动

Application(baseApp)->run InitService(异步执行) ->init通知栏 渠道 ->MainActivity onCreate 里面创建通知栏 在主线程执行

第一个办法 initservice之后再启动服务

遇到问题：Voiceservice 的方法一直无法调用 通知栏无法显示

排查解决：发现自己在前面因为的一个报错 以为是service添加到清单文件的错误 导致service被自己删除 而没有还原 所以这个错误就是service没有在清单文件声明造成的

### 语音合成配置

![image-20240625213922009](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202406252139110.png)

唤醒功能百度给的demo跟成品的App都暂时无法唤醒 考虑到 没有必要浪费这么多时间去一直修改唤醒的问题 所以暂时跳过这个功能

## 网络

### retrofit封装

* 创建retrofit对象
* 构建代理服务类
* 实现请求方法

### 拦截器

## 主页框架

在viewpager2当中 item当中的布局文件的高和宽 必须设置为 match_parent

也可以在适配器当中设置

### 层叠效果

CommonAdapter

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CommonViewHolder {
        var layoutId = onBindDataListener?.getLayoutId(viewType)
        var layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return CommonViewHolder.getViewHolder(parent,layoutId!!,layoutParams)

    }
```

CommonViewHolder

```kotlin
fun getViewHolder(parent: ViewGroup, layoutId: Int, layoutParams: ViewGroup.LayoutParams): CommonViewHolder {
            var itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            if (layoutParams!= null){
                itemView.layoutParams = layoutParams
            }
            var viewHolder = CommonViewHolder(
                itemView
            )
            return viewHolder
        }
```

## 	WindowManager 对话窗口

### 窗口权限

### 窗口实现

### 对话列表实现





### 部分语义理解

#### 天气

今天<地名>的天气如何（怎样）

明天<地名>的天气如何（怎样）

今天（明天）<地名>适合穿什么衣服

今天（明天）<地名>的风力多少级

。。。

#### app操作

卸载<app名称>

更新<app名称>

打开<app名称>

#### 笑话

给我讲个笑话





今天北京的天气
