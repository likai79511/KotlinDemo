package com.agera.thirdplatfomdemo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.agera.thirdplatfomdemo.Login.Platform
import com.agera.thirdplatfomdemo.bean.WBUserInfo
import com.google.gson.Gson

/**
 * Created by Agera K
 */
class InfoActivity : Activity() {
    var mTv_type: TextView? = null
    var mTv_nickName: TextView? = null
    var mImg_head: ImageView? = null
    var platform: Platform? = null
    var info: String? = null
    var mGson: Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.info)
        initView()
        initData()
        displayInfo()
    }

    private fun displayInfo() {
        when (platform) {
            Platform.Sina -> {
                var userInfo = mGson.fromJson(info, WBUserInfo::class.java)
                mTv_type!!.text = "Type: ${platform.toString()}"
                mTv_nickName!!.text = userInfo.name
                Log.e("---","---thread id: ${info}")
                Utils.instance().setImageResourse(mImg_head!!,userInfo.profile_image_url)
            }
        }
    }

    private fun initData() {
        intent?.let {
            platform = it.getSerializableExtra("platform") as Platform
            info = it.getStringExtra("info")
        }
    }

    private fun initView() {
        mImg_head = findViewById(R.id.img_head) as ImageView
        mTv_nickName = findViewById(R.id.tv_nickname) as TextView
        mTv_type = findViewById(R.id.tv_type) as TextView
    }


}