package com.example.lib_base.base

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * @description: TODO 父类Activity
 * @author: mlf
 * @date: 2024/6/20 16:53
 * @version: 1.0
 */
abstract class BaseActivity<T: ViewBinding > : AppCompatActivity() {
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
                it.setDisplayShowHomeEnabled(isShowBack())
                it.elevation=0f
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
}