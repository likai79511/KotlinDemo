package com.agera.thirdplatfomdemo.http

import android.widget.ImageView
import com.google.android.agera.net.HttpRequest
import com.google.android.agera.net.HttpRequests

/**
 * Created by Agera K
 */
class RESTful private constructor(){
    companion object {
        val timeout: Int = 5_000

        /*
         *  For Sina WeiBo Constants
         */
        val WB_TOKEN: String = "access_token"
        val WB_UID: String = "uid"
        val WB_USER_INFO_URL = "https://api.weibo.com/2/users/show.json"
        /**
         * For SinaWeiBo: get User information
         */
        fun getWeiBoUserInfo(token: String, uid: String): HttpRequest {
            return HttpRequests.httpGetRequest("$WB_USER_INFO_URL?$WB_TOKEN=$token&$WB_UID=$uid")
                    .connectTimeoutMs(timeout)
                    .readTimeoutMs(timeout)
                    .compile()
        }

        var url = "https://graph.qq.com/user/get_user_info"
        var appId: String = "1106465319"
        fun getQQUserInfo(value:Pair<String,String>):HttpRequest{
            return HttpRequests.httpGetRequest("$url?access_token=${value.first}&oauth_consumer_key=$appId&openid=${value.second}")
                    .connectTimeoutMs(5_000)
                    .readTimeoutMs(5_000)
                    .compile()
        }
    }
}