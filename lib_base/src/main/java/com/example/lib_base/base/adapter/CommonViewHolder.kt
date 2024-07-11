package com.example.lib_base.base.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @description: TODO 通用的Adapter
 * @author: mlf
 * @date: 2024/6/24 19:10
 * @version: 1.0
 */
class CommonViewHolder(itemView: View):
    RecyclerView.ViewHolder(itemView) {
        private var sparseArray = SparseArray<View>()
    companion object {
        fun getViewHolder(parent: ViewGroup, layoutId: Int): CommonViewHolder {
            var itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            var viewHolder = CommonViewHolder(
                itemView
            )
            return viewHolder
        }
    }
    fun <T: View> getView(id: Int): T {
        var view = sparseArray[id]
        if (view == null) {
            view = itemView.findViewById(id)
            sparseArray.put(id, view)
        }
        return view!! as T
    }



}