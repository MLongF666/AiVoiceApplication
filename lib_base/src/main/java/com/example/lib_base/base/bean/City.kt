package com.example.lib_base.base.bean

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/8/3 17:03
 * @version: 1.0
 */
data class City(
    val error_code: Int,
    val reason: String,
    val result: List<Result>
)

data class Result(
    val type: Int,
    val city: String,
    val district: String,
    val id: String,
    val province: String
)