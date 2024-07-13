package com.example.lib_base.helper

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager

/**
 * @description: TODO 窗口帮助类
 * @author: mlf
 * @date: 2024/7/10 22:28
 * @version: 1.0
 */
object WindowsHelper {
    private lateinit var mContext: Context
    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams


    fun initHelper(context: Context) {
        mContext = context
        mWindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams=createLayoutParams()
    }

    private fun createLayoutParams(): WindowManager.LayoutParams {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.apply {
            this.width = WindowManager.LayoutParams.MATCH_PARENT
            this.height = WindowManager.LayoutParams.MATCH_PARENT
            gravity = Gravity.TOP or Gravity.LEFT
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            type=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            }else{
                WindowManager.LayoutParams.TYPE_PHONE
            }
        }
        return layoutParams
    }
    //获取视图控件
    fun getView(layoutId: Int): View {
        return View.inflate(mContext, layoutId, null)
    }

    //显示窗口
    fun showView(view: View) {
        if (view.parent == null){
            mWindowManager.addView(view, mLayoutParams)
        }
    }
    //显示窗口 子定义
    fun showView(view: View,lp:WindowManager.LayoutParams) {
        if (view.parent == null){
            mWindowManager.addView(view, lp)
        }
    }
    //隐藏视图
    fun hideView(view: View) {
        if (view.parent != null){
            mWindowManager.removeView(view)
        }
    }
    //更新视图
    fun updateView(view: View,lp:WindowManager.LayoutParams) {
        if (view.parent != null){
            mWindowManager.updateViewLayout(view, lp)
        }
    }

}