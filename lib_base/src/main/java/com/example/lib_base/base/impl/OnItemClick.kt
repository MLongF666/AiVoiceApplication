package com.example.lib_base.base.impl

import android.view.View
import com.example.lib_base.base.adapter.CommonViewHolder

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/7/10 19:28
 * @version: 1.0
 */
interface OnItemClick<T> {
    fun onItemClick(position: Int,view: View,t:T)
}