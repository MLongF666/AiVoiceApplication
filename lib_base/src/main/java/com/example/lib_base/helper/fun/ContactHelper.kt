package com.example.lib_base.helper.`fun`

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import com.example.lib_base.helper.`fun`.data.ContactData
import com.example.lib_base.utils.L

/**
 * @description: TODO 联系人帮助类/权限
 * @author: mlf
 * @date: 2024/7/21 9:57
 * @version: 1.0
 */
@SuppressLint("StaticFieldLeak")
object ContactHelper {
    private lateinit var mContext: Context
//    const val PHONE_URI = "content://com.android.contacts/contacts"
    private val PHONE_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
    //查询条件 名称-号码
    private const val NAME=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    private const val NUMBER=ContactsContract.CommonDataKinds.Phone.NUMBER
    private lateinit var mContactList: ArrayList<ContactData>
    //初始化
    fun init(context: Context) {
        mContext = context
        loadContacts()
    }

    //加载联系人
    @SuppressLint("Range")
    private fun loadContacts() {
        mContactList= ArrayList<ContactData>()
        var resolver = mContext.contentResolver
        var cursor = resolver.query(PHONE_URI, arrayOf(NAME, NUMBER), null, null, null)
        cursor?.let {
            while (it.moveToNext()) {
                val contactData =ContactData(
                    it.getString(it.getColumnIndex(NAME)),
                    it.getString(it.getColumnIndex(NUMBER))
                )
                mContactList.add(contactData)
            }
            L.d("mContactList: $mContactList")
        }
        cursor?.close()
    }
    //拨打电话
    fun callPhone(phoneNumber: String) {
        //TODO
        Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
            mContext.startActivity(this)
        }
    }

    fun getContactList(): ArrayList<ContactData> {
        return this.mContactList
    }
}