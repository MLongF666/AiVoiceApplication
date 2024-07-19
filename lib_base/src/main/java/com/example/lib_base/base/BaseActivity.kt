package com.example.lib_base.base

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.lib_base.R
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import java.security.Permission


/**
 * @description: TODO 父类Activity
 * @author: mlf
 * @date: 2024/6/20 16:53
 * @version: 1.0
 */
abstract class BaseActivity<T: ViewBinding > : AppCompatActivity() {
    protected val WINDOW_PERMISSION=1000
    private lateinit var binding: T
    fun getBinding(): T {
        return binding
    }
    abstract fun getTitleText(): String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=initViewBinding()
        setContentView(binding.root)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            //设置标题 如果getTitleText不为空就赋值给title
            supportActionBar?.let {
                it.title=getTitleText()
                it.elevation=0f
                it.setHomeButtonEnabled(isShowBack())
                it.setDisplayHomeAsUpEnabled(isShowBack())

            }
        }
        initView()
        initData()
        initEvent()
    }

    override fun onStart() {
        super.onStart()

    }

    abstract fun initEvent()

    abstract fun initData()

    abstract fun initViewBinding(): T

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        return true
    }

    abstract fun isShowBack(): Boolean

    abstract fun initView()

    //检查窗口权限
    protected fun checkWindowPermission():Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this)
        }
        return true
    }
    //申请窗口权限
    protected fun requestWindowPermission(packageName:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivityForResult(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                ), WINDOW_PERMISSION
            )
        }
    }
    //检查权限
    protected fun checkPermission(permission:String):Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return checkSelfPermission(permission)== PackageManager.PERMISSION_GRANTED
        }
        return true
    }
    //请求权限
    protected fun requestPermission(permissions:Array<String>, granted:Action<List<String>> ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //申请权限
            AndPermission.with(this)
                .runtime()
                .permission(permissions)
                .onGranted(granted)
                .start()
        }
    }


}