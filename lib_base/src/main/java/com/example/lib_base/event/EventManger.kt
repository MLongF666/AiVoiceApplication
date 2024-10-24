package com.example.lib_base.event

import org.greenrobot.eventbus.EventBus

/**
 * @author: mlf
 * @Date: 2021/4/20
 * @Description: EventBus管理
 */
object EventManger {
    //注册
    fun register(subscriber: Any) {
        //any等同于java object
        EventBus.getDefault().register(subscriber)
    }

    //注销
    fun unregister(subscriber: Any) {
        EventBus.getDefault().unregister(subscriber)
    }
    //发送事件
    private fun post(event: MessageEvent) {
        EventBus.getDefault().post(event)
    }
    //发送类型
    fun post(type: Int) {
        post(MessageEvent(type))
    }
    //发送类型和字符串
    fun post(type: Int, str: String) {
        val event = MessageEvent(type)
        event.strValue=str
        post(event)
    }
    //发送类型和对象
    fun post(type: Int, obj: Any) {
        val event = MessageEvent(type)
        event.objValue=obj
        post(event)
    }
    //发送类型和Int
    fun post(type: Int, int: Int) {
        val event = MessageEvent(type)
        event.intValue=int
        post(event)
    }
    //发送类型和Long
    fun post(type: Int, long: Long) {
        val event = MessageEvent(type)
        event.longValue=long
        post(event)
    }
    //发送类型和Boolean
    fun post(type: Int, bool: Boolean) {
        val event = MessageEvent(type)
        event.boolValue=bool
        post(event)
    }



}