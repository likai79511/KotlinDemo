package com.agera.thirdplatfomdemo.Login

import android.app.Activity
import android.util.Log
import android.view.View
import com.agera.thirdplatfomdemo.core.TaskDriver
import com.agera.thirdplatfomdemo.http.HttpCallable
import com.agera.thirdplatfomdemo.http.HttpTask
import com.agera.thirdplatfomdemo.http.RESTful
import com.google.android.agera.BaseObservable
import com.google.android.agera.Result
import com.google.android.agera.Supplier
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler

/**
 * Created by Agera K
 */
class WBEvent constructor(var activity: Activity) : BaseObservable(), WbAuthListener, View.OnClickListener, Supplier<Result<String>> {
    var WbHandler: SsoHandler? = null
    var result: Result<String> = Result.failure()

    override fun onClick(v: View?) {
        if (WbHandler == null)
            WbHandler = SsoHandler(activity)

        WbHandler?.let { it.authorize(this) }
    }

    override fun onSuccess(p0: Oauth2AccessToken?) {
        p0?.let {
            if (it.isSessionValid) {
                var task = HttpTask(HttpCallable(RESTful.getWeiBoUserInfo(it.token, it.uid)))
                TaskDriver.instance().execute(task)
                task.get()
                        .ifFailedSendTo { result = Result.failure() }
                        .ifSucceededSendTo { body ->
                            result = Result.success(body.bodyString.get())
                            dispatchUpdate()
                        }
            }
        }
    }

    override fun onFailure(p0: WbConnectErrorMessage?) {
        Log.e("---", "---weibo auth onFailure $p0")
    }

    override fun cancel() {
        Log.e("---", "---weibo auth cancel")
    }

    override fun get(): Result<String> {
        return result
    }
}