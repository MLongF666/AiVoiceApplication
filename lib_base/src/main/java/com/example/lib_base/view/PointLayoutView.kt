package com.example.lib_base.view

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.lib_base.R

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/7/12 19:04
 * @version: 1.0
 */
class PointLayoutView:LinearLayout {
    private val mListImageView=ArrayList<ImageView>()
    constructor(context:Context):super(context){
        init()
    }

    constructor(context:Context,attrs:android.util.AttributeSet):
            super(context,attrs){init()}

    constructor(context: Context, attrs:android.util.AttributeSet,
                defStyleAttr:Int):super(context,attrs,defStyleAttr){init()}
    private fun init() {
        //TODO 初始化
        orientation=HORIZONTAL
        gravity=Gravity.CENTER
    }
    //设置页面数量
    fun  setPointCount(count:Int){
        if (mListImageView.size>0){
            mListImageView.clear()
        }
        for (i in 0 until count){
            val imageView = ImageView(context)
            addView(imageView)
            mListImageView.add(imageView)
        }
        //默认设置第一页
        setSelect(0)
    }
    //设置选中
    fun setSelect(index:Int){
        if (index>mListImageView.size-1){
            return
        }
        for (i in 0 until mListImageView.size){
            if (i==index) mListImageView[i].setImageResource(R.drawable.shape_point_select)
            else{
                mListImageView[i].setImageResource(R.drawable.shape_point_normal)
            }
        }
    }

}