package com.example.lib_network.bean

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/7/31 9:17
 * @version: 1.0
 */
data class JokeDataBean(
    val code: Int,
    val msg: String,
    val result: JokeResult
)

data class JokeResult(
    val list: List<Item>
)

data class Item(
    val content: String,
    val title: String
)