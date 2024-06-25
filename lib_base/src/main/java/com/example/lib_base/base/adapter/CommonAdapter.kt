package com.example.lib_base.base.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


/**
 * @description: TODO 通用的Adapter
 * @author: mlf
 * @date: 2024/6/24 19:09
 * @version: 1.0
 */
class CommonAdapter<T> : RecyclerView.Adapter<CommonViewHolder> {
    constructor(data: List<T>, listener: OnBindDataListener<T>){
        this.mData = data
        this.onBindDataListener = listener
    }
    constructor(data: List<T>, listener: OnMoreBindDataListener<T>){
        this.mData = data
        this.onBindDataListener=listener
        this.onMoreBindDataListener = listener

    }
    //数据
    private lateinit var mData: List<T>
    //接口
    private  var onBindDataListener: OnBindDataListener<T>?=null
    private  var onMoreBindDataListener: OnMoreBindDataListener<T>?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CommonViewHolder {
        var layoutId = onBindDataListener?.getLayoutId(viewType)
        return CommonViewHolder.getViewHolder(parent,layoutId!!)

    }

    override fun getItemCount(): Int {
        return mData.size
    }
    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        onBindDataListener?.onBindViewHolder(mData[position],holder,getItemViewType(position),position)
    }

    override fun getItemViewType(position: Int): Int {
        if (onMoreBindDataListener!=null){
            return onMoreBindDataListener!!.getItemViewType(position)
        }
        return 0
    }
    interface OnBindDataListener<T> {
       fun onBindViewHolder(model:T,viewHolder:CommonViewHolder,type:Int,position:Int)
//       fun getViewBinding(viewBinding: ViewBinding):ViewBinding
       fun getLayoutId(type:Int):Int
    }
    interface OnMoreBindDataListener<T>:OnBindDataListener<T> {
        fun getItemViewType(position:Int):Int

    }


}