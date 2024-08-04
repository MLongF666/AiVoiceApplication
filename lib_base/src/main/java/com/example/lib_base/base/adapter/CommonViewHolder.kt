package com.example.lib_base.base.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @description: TODO 通用的Adapter
 * @author: mlf
 * @date: 2024/6/24 19:10
 * @version: 1.0
 */
open class CommonViewHolder(itemView: View):
    RecyclerView.ViewHolder(itemView) {
        private var sparseArray = SparseArray<View>()
    companion object {
        fun getViewHolder(parent: ViewGroup, layoutId: Int): CommonViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            val viewHolder = CommonViewHolder(
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

    fun setText(view: Int, msg: String) {
        getView<TextView>(view).text = msg
    }


}