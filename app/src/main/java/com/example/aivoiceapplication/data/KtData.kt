package com.example.aivoiceapplication.data

/**
 * @description: TODO 数据类
 * @author: mlf
 * @date: 2024/7/10 18:30
 * @version: 1.0
 */
data class MainListData(val title: String,
                        val color: Int,
                        val icon: Int,
                        val description:String)

data class ChatListData(
    val type:Int
){
    lateinit var text: String
    lateinit var wid:String
    lateinit var info:String
    lateinit var city:String
    lateinit var temperature:String
}