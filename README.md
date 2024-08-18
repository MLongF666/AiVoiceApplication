## 本项目核心思想

Kotlin+组件化+kotlin Gradle DSL

## 组件化架构思想

![AI语音助手客户端架构图](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202406261058116.png)

### 业务模块

| 笑话       | ![微信图片_20240818133202](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181337458.jpg) | ![微信图片_20240818133157](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181337315.jpg) |
| ---------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 地图       | ![微信图片_20240818133136](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181338326.jpg) |                                                              |
| 星座       |                                                              |                                                              |
| 语音设置   | ![微信图片_20240818133146](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181338661.jpg) |                                                              |
| 系统设置   | ![微信图片_20240818133141](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181338850.jpg) |                                                              |
| 天气       |                                                              |                                                              |
| 应用管理   | ![微信图片_20240818133151](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181338320.jpg) |                                                              |
| 开发者模式 |                                                              |                                                              |



* 笑话

* 地图
* 星座
* 语音设置
* 系统设置
* 天气
* 应用管理
* 开发者模式

### AI

* TTS/发音
* ASR/语义识别
* WakeUp唤醒

### 高德地图SDK

* 定位
* 导航
* poi搜索

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
