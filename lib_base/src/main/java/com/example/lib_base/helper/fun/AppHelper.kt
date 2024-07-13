package com.example.lib_base.helper.`fun`

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.lib_base.R
import com.example.lib_base.helper.`fun`.data.AppData
import com.example.lib_base.utils.L

/**
 * @description: TODO App帮助类
 * @author: mlf
 * @date: 2024/7/11 20:45
 * @version: 1.0
 */
object AppHelper {
    private lateinit var mContext: Context
    //包管理器
    private lateinit var pm: PackageManager
    //所有app列表
    private val mAllAppList = ArrayList<AppData>()
    //分页列表
    private val mViewList = ArrayList<View>()
    fun initHelper(mContext: Context){
        this.mContext = mContext
        pm = mContext.packageManager
        loadAllApp()
    }

    private fun initPageView() {
        getPageSize()
        //遍历所有Apk对象数量
        for (i in 0 until getPageSize()) {
            //->FrameLayout
            var rootView =
                View.inflate(mContext, R.layout.layout_app_manager_item, null) as ViewGroup
            //第一层线性布局
            for (j in 0 until rootView.childCount){
                //得到第二层6个线性布局
                var childAt = rootView.getChildAt(j) as ViewGroup
                for (k in 0 until childAt.childCount){
                    //得到第三层4个线性布局
                    var childAt1 = childAt.getChildAt(k) as ViewGroup
                    //ImageView
                    var imageView = childAt1.getChildAt(0) as ImageView
                    //TextView
                    var textView = childAt1.getChildAt(1) as TextView
                    //获取应用下标
                    var index = i*24+j*4+k
                    if (index<mAllAppList.size){
                        //TODO 设置数据
                        imageView.setImageDrawable(mAllAppList[index].appIcon)
                        textView.text=mAllAppList[index].appName
                        //设置点击事件
                        childAt1.setOnClickListener {
                            startApp(mAllAppList[index].packageName)
                        }
                    }
                }
            }
            mViewList.add(rootView)
        }
    }

    //获取分页数
    public fun getPageSize():Int {
        return mAllAppList.size/24+1
    }
    //获取所有app视图列表
    fun getAllViewList(): ArrayList<View> {
        return this.mViewList
    }

    private fun loadAllApp(){
        //TODO 加载所有app
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        var appInfo = pm.queryIntentActivities(intent, 0)
        appInfo.forEachIndexed { _, resolveInfo ->
            val appData=AppData(
                    resolveInfo.activityInfo.packageName,
                    resolveInfo.loadLabel(pm) as String,
                    resolveInfo.loadIcon(pm),
                    resolveInfo.activityInfo.name,
                    resolveInfo.activityInfo.flags == ApplicationInfo.FLAG_SYSTEM)
            mAllAppList.add(appData)
        }
            initPageView()
            L.e("appData: $mAllAppList")
        }
    //启动App
    fun launchApp(appName: String):Boolean {
        if(mAllAppList.size>0){
            mAllAppList.forEach {
                if (it.appName == appName){
                    startApp(it.packageName)
                    return true
                }
            }
        }
        return false
    }
    //卸载App
    fun uninstallApp(appName: String):Boolean {
        if(mAllAppList.size>0){
            mAllAppList.forEach {
                if (it.appName == appName){
                    //TODO 卸载app
                    intentUninstallApp(it.packageName)
                    return true
                }
            }
        }
        return false
    }

    //启动卸载App
    private fun intentUninstallApp(packageName: String) {
        val intent=Intent(Intent.ACTION_DELETE)
        intent.data=Uri.parse("package:$packageName")
        mContext.startActivity(intent)

    }
    //获取非系统应用
    fun getNotSystemApp():List<AppData> {
        return mAllAppList.filter { !it.isSystemApp }
    }

    //启动App
    private fun startApp(packageName: String) {
        val intent=pm.getLaunchIntentForPackage(packageName)
        intent?.let {
            it.flags=Intent.FLAG_ACTIVITY_NEW_TASK
            mContext.startActivity(it)
        }
    }
    //跳转应用商店
    fun intentAppStore(packageName: String,markPackageName:String){
        val uri = Uri.parse("market://details?id=${packageName}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage(markPackageName)
        mContext.startActivity(intent)
    }
}