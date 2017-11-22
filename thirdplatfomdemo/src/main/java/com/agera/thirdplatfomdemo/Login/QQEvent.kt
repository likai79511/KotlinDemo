package com.agera.thirdplatfomdemo.Login

import android.app.Activity
import android.view.View
import com.agera.thirdplatfomdemo.bean.Status
import com.agera.thirdplatfomdemo.core.TaskDriver
import com.agera.thirdplatfomdemo.http.HttpCallable
import com.agera.thirdplatfomdemo.http.HttpTask
import com.agera.thirdplatfomdemo.http.RESTful
import com.google.android.agera.BaseObservable
import com.google.android.agera.Receiver
import com.google.android.agera.Result
import com.google.android.agera.Supplier
import com.google.gson.Gson
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError

/**
 * Created by Agera K
 */
class QQEvent constructor(var activity: Activity) : BaseObservable(), IUiListener, View.OnClickListener, Supplier<Result<String>>, Receiver<Pair<String, String>> {
    var mTencent: Tencent? = null
    var appId: String = "1106465319"
    var task: HttpTask? = null

    init {
        mTencent = Tencent.createInstance(appId, activity)
    }

    override fun onComplete(p0: Any?) {
    }

    override fun onCancel() {
    }

    override fun onError(p0: UiError?) {
    }

    override fun get(): Result<String> {
        var result: Result<String> = Result.failure<String>()
        task?.let {
            it.get()
                    .ifFailedSendTo { Result.failure<String>() }
                    .ifSucceededSendTo { body ->
                        var status = Gson().fromJson(body.bodyString.get(), Status::class.java)
                        if (status.ref == 0) {
                            result = body.bodyString!!
                        } else {
                            result = Result.failure<String>()
                        }
                    }
        }
        return result
    }

    override fun onClick(v: View?) {
        mTencent?.login(activity, "all", this)
    }

    override fun accept(value: Pair<String, String>) {
        task = HttpTask(HttpCallable(RESTful.getQQUserInfo(value)))
        TaskDriver.instance().execute(task!!)
        dispatchUpdate()
    }
}