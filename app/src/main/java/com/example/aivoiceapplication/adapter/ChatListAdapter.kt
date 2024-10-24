package com.example.aivoiceapplication.adapter

import android.widget.ImageView
import android.widget.TextView
import com.example.aivoiceapplication.R
import com.example.aivoiceapplication.data.ChatListData
import com.example.lib_base.entity.AppConstants
import com.example.lib_base.base.adapter.CommonAdapter
import com.example.lib_base.base.adapter.CommonViewHolder
import com.example.module_weather.tools.WeatherIconTools

/**
 * @description: TODO 聊天列表·适配器
 * @author: mlf
 * @date: 2024/7/11 11:18
 * @version: 1.0
 */
class ChatListAdapter(data: List<ChatListData>) :
    CommonAdapter<ChatListData>(data, object :CommonAdapter.OnMoreBindDataListener<ChatListData> {
        override fun onBindViewHolder(
            model: ChatListData,
            viewHolder: CommonViewHolder,
            type: Int,
            position: Int
        ) {
            when(type){
                AppConstants.TYPE_MINE_TEXT -> viewHolder.getView<TextView>(R.id.tv_mine_content).text = model.text
                AppConstants.TYPE_AI_TEXT -> viewHolder.getView<TextView>(R.id.tv_chat_ai).text = model.text
                AppConstants.TYPE_WEATHER_TEXT -> {
                    viewHolder.getView<TextView>(R.id.tv_city).text=model.city
                    viewHolder.getView<TextView>(R.id.tv_info).text=model.info
                    viewHolder.getView<TextView>(R.id.tv_temperature).text=model.temperature
                    viewHolder.getView<ImageView>(R.id.iv_icon).setImageResource(WeatherIconTools.getIcon(model.wid))
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return data[position].type
        }

        override fun getLayoutId(type: Int): Int {
           return when(type){
               AppConstants.TYPE_MINE_TEXT -> R.layout.item_chat_list_mine
               AppConstants.TYPE_AI_TEXT -> R.layout.item_chat_list_ai
               AppConstants.TYPE_WEATHER_TEXT -> R.layout.item_chat_list_weather
               else -> 0
           }
        }

    }) {
}