package com.example.module_weather.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @description: TODO 城市导航View
 * @author: mlf
 * @date: 2024/8/4 14:58
 * @version: 1.0
 */
class CitySelectView: View {
    //view高
    private var viewHeight: Int = 0

    private var viewWidth: Int = 0
    //view宽

    //画笔
    private val paint by lazy { Paint() }

    //数据源
    private val mList=ArrayList<String>()
    //选中的下标
    private var selectIndex:Int=-1
    //选中的文本大小
    private var selectTextSize:Float=30f
    //未选中的文本大小
    private var unSelectTextSize:Float=20f
    //选中的文本颜色
    private var selectTextColor:Int=Color.RED
    //未选中的文本颜色
    private var unSelectTextColor:Int=Color.BLACK
    //间隔的高
    private var itemHeight: Int = 0
    constructor(context: Context?) : super(context){
        initView()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){initView()}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){initView()}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h
        viewWidth = w
    }
    private fun initView(){
        //抗锯齿
        paint.isAntiAlias = true

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCity(canvas)


    }


    //绘制城市
    private fun drawCity(canvas: Canvas) {
        if (mList.size>0){
            //得到每个城市文本的高度
            itemHeight = viewHeight/mList.size
            //遍历数据源
            mList.forEachIndexed { index, text ->
                if (index==selectIndex){
                    paint.color=selectTextColor
                    paint.textSize=selectTextSize
                }else{
                    paint.color=unSelectTextColor
                    paint.textSize=unSelectTextSize
                }
                //绘制文本 宽度-画笔绘制text的宽度 然后再除以2
                val textX=(viewWidth-paint.measureText(text))/2
                val textY=(itemHeight*index+itemHeight).toFloat()
                //绘制文本
                canvas.drawText(text,textX,textY,paint)

            }
        }
    }



    //设置数据源
    fun setCityList(list: List<String>){
        if (mList.size>0){
            mList.clear()
        }
        mList.addAll(list)
        invalidate()
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it.action){
                MotionEvent.ACTION_DOWN->{
                    mListener?.uiChange(true)
                }
                MotionEvent.ACTION_MOVE->{
                    //这里做处理
                    //当手指按下去的时候 如何计算选中的下标
                    val oldIndex=selectIndex
                    val check=event.y/viewHeight*mList.size
                    if (oldIndex!=check.toInt()){
                        mListener?.valueInput(mList[check.toInt()])
                        //往外传递数据
                        selectIndex=check.toInt()
                        invalidate()
                    } else {
                        //重复
                    }
                }
                MotionEvent.ACTION_UP->{
                    mListener?.uiChange(false)
                }
                else -> {}
            }
        }

        return super.dispatchTouchEvent(event)
    }
    //===================OpenImpl=========================
    private var mListener:OnViewResultListener?=null
    interface OnViewResultListener{
        //外部控件隐藏 显示
        fun uiChange(show:Boolean)
        //传递值
        fun  valueInput(value:String)
    }
    fun setOnViewResultListener(listener:OnViewResultListener){
        mListener=listener
    }

    fun setCityIndex(itemIndex: Int) {
        selectIndex=itemIndex
        invalidate()
    }
}