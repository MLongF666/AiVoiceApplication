## 本项目核心思想

Kotlin+组件化+kotlin Gradle DSL

## 组件化架构思想

![AI语音助手客户端架构图](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202406261058116.png)

### 业务模块

| 笑话       | ![微信图片_20240818133202](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181337458.jpg) | ![微信图片_20240818133157](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181337315.jpg) |
| ---------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 地图       | ![微信图片_20240818133136](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181338326.jpg) |                                                              |
| 星座       |                                                              |                                                              |
| 语音设置   | ![微信图片_20240818133146](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181338661.jpg) | ![微信图片_20240819095213](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408190953690.jpg) |
| 系统设置   | ![微信图片_20240818133141](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181338850.jpg) |                                                              |
| 天气       |                                                              |                                                              |
| 应用管理   | ![微信图片_20240818133151](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408181338320.jpg) | ![微信图片_20240819095323](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408190953356.jpg) |
| 开发者模式 | ![微信图片_20240818133119](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408190952496.jpg) | ![微信图片_20240819095207](https://cdn.jsdelivr.net/gh/mlf0214/blogImage@main/img/202408190952234.jpg) |



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

#### 地图



