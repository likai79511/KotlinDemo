package com.agera.thirdplatfomdemo

import android.app.Application
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo

/**
 * Created by Agera K
 */
class MyApp : Application() {
    val WeiBo_APP_KEY: String = "3525150991"
    val WeiBo_REDIRECT_URL = "http://www.sina.com"
    val WeiBo_SCOPE: String? = null

    override fun onCreate() {
        super.onCreate()

        WbSdk.install(this,AuthInfo(this,WeiBo_APP_KEY,WeiBo_REDIRECT_URL,WeiBo_SCOPE))

    }
}