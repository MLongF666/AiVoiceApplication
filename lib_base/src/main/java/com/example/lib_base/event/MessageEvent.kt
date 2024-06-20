package com.example.lib_base.event

/**
 * @description: TODO
 * @author: mlf
 * @date: 2024/6/20 21:11
 * @version: 1.0
 */
class MessageEvent(val type:Int) {
    var strValue:String=""
    var intValue:Int=0
    var objValue:Any?=null
    var longValue:Long=0
    var boolValue:Boolean=false
}