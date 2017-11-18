package com.agera.thirdplatfomdemo.http

import com.google.android.agera.net.HttpRequest
import com.google.android.agera.net.HttpRequests

/**
 * Created by Administrator on 2017/11/17 0017.
 */
class RESTful {
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
    }
}